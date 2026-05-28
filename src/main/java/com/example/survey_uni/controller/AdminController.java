package com.example.survey_uni.controller;

import com.example.survey_uni.dto.request.QuestionCreateRequest;
import com.example.survey_uni.dto.request.QuestionUpdateRequest;
import com.example.survey_uni.dto.request.SurveyCreateRequest;
import com.example.survey_uni.dto.request.SurveyStatusRequest;
import com.example.survey_uni.dto.response.ClassOfferingMinifiedResponse;
import com.example.survey_uni.dto.response.CourseMinifiedResponse;
import com.example.survey_uni.dto.response.SurveyDetailAdminResponse;
import com.example.survey_uni.dto.response.SurveyListResponse;
import com.example.survey_uni.model.User;
import com.example.survey_uni.repository.UserRepository;
import com.example.survey_uni.service.CourseService;
import com.example.survey_uni.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private SurveyService surveyService;
    @Autowired
    private CourseService courseService;


    // API tạo tài khoản admin khi test
    @PostMapping("/create")
    @Operation(
            summary = "Tạo tài khoản admin",
            description = "API dùng để tạo tài khoản admin khi mới khởi tạo hệ thống"
    )
    public ResponseEntity<?> createAdmin() {
        if (userRepository.findByEmail("admin@gmail.com").isEmpty()) {
            User user = new User();
            user.setUsername("admin");
            user.setEmail("admin@gmail.com");
            user.setPassword(passwordEncoder.encode("123456"));
            user.setRole(User.Role.ADMIN);
            user.setStatus(User.Status.ACTIVE);
            user.setGender(User.Gender.MALE);
            userRepository.save(user);
            return ResponseEntity.ok("Admin account created!");
        }
        return ResponseEntity.badRequest().body("Admin already exists");
    }


    // API Tạo khảo sát và bộ câu hỏi
    @PostMapping("/surveys/create")
    @Operation(
            summary = "Tạo mới một cuộc khảo sát kèm bộ câu hỏi",
            description = "Khởi tạo một cuộc khảo sát mới gắn liền với một lớp học phần. Danh sách bộ câu hỏi đính kèm trong body sẽ tự động được lưu xuống cơ sở dữ liệu thông qua cơ chế Cascade."
    )
    public ResponseEntity<Map<String, Object>> createSurvey(@RequestBody SurveyCreateRequest request) {

        Integer newSurveyId = surveyService.createSurveyWithQuestions(request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Khởi tạo đợt khảo sát và bộ câu hỏi thành công.");
        response.put("surveyId", newSurveyId);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // API Lấy danh sách môn học phục vụ hiển thị Menu / Search thanh gợi ý
    // GET: /api/v1/admin/surveys/courses-menu?keyword=IT
    @GetMapping("/courses-menu")
    @Operation(
            summary = "Lấy danh sách môn học cho menu gợi ý",
            description = "Trả về danh sách môn học rút gọn (ID, Mã môn, Tên môn) phục vụ cho thanh tìm kiếm gợi ý hoặc Dropdown trên giao diện tạo khảo sát. Hỗ trợ lọc theo từ khóa (keyword) không phân biệt hoa thường."
    )
    public ResponseEntity<List<CourseMinifiedResponse>> getCoursesForMenu(
            @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {

        List<CourseMinifiedResponse> courses = courseService.getCoursesForMenu(keyword);
        return ResponseEntity.ok(courses);
    }


    // API Lấy danh sách lớp học phần theo môn học (cho học kỳ hiện tại và chưa có khảo sát)
    @GetMapping("/class-offerings")
    @Operation(
            summary = "Lấy danh sách lớp học phần theo môn học",
            description = "Trả về danh sách các lớp học phần thuộc về một môn học cụ thể. Hệ thống tự động lọc theo học kỳ đang hoạt động (ACTIVE) và áp dụng ràng buộc nghiệp vụ: chỉ hiển thị các lớp CHƯA TỪNG được gán khảo sát nhằm tránh tạo trùng."
    )    public ResponseEntity<List<ClassOfferingMinifiedResponse>> getClassOfferings(
            @RequestParam("courseId") Integer courseId) {

        List<ClassOfferingMinifiedResponse> offerings = courseService.getClassOfferingsByCourse(courseId);
        return ResponseEntity.ok(offerings);
    }


     // API Xem danh sách các cuộc khảo sát đã tạo
    @GetMapping("/surveys")
    @Operation(
            summary = "Lấy danh sách tất cả các cuộc khảo sát",
            description = "Trả về danh sách toàn bộ các cuộc khảo sát đã tạo trong hệ thống kèm thông tin tổng quan về môn học, lớp học phần và giảng viên phụ trách để hiển thị lên bảng quản lý."
    )
    public ResponseEntity<List<SurveyListResponse>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.getAllSurveys());
    }


    // API Xem chi tiết 1 cuộc khảo sát (Bao gồm bộ câu hỏi)
    @GetMapping("/surveys/{survey-id}")
    @Operation(
            summary = "Xem chi tiết một cuộc khảo sát",
            description = "Lấy trọn vẹn thông tin chi tiết của một cuộc khảo sát dựa trên ID cuộc khảo sát được truyền vào, bao gồm cả cấu trúc danh sách bộ câu hỏi chi tiết đi kèm."
    )
    public ResponseEntity<SurveyDetailAdminResponse> getSurveyDetail(@PathVariable("survey-id") Integer id) {
        return ResponseEntity.ok(surveyService.getSurveyDetail(id));
    }

    // API Thay đổi trạng thái đóng/mở khảo sát thủ công
    @PutMapping("/surveys/{survey-id}/status")
    @Operation(
            summary = "Thay đổi trạng thái đóng/mở khảo sát thủ công",
            description = "Cho phép quản trị viên can thiệp bật/tắt (true/false) trạng thái hoạt động của một cuộc khảo sát bất kỳ lúc nào, dùng trong các trường hợp cần đóng khảo sát khẩn cấp hoặc mở lại cho sinh viên làm bù."
    )
    public ResponseEntity<Map<String, Object>> updateSurveyStatus(
            @PathVariable("survey-id") Integer id,
            @RequestBody SurveyStatusRequest request) {

        surveyService.updateSurveyStatus(id, request.getActive());

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Cập nhật trạng thái cuộc khảo sát thành công.");
        response.put("currentStatus", request.getActive() ? "ĐANG MỞ" : "ĐÃ ĐÓNG");

        return ResponseEntity.ok(response);
    }

    // API Chỉnh sửa nội dung 1 câu hỏi khảo sát
    @PutMapping("/questions/{question-id}")
    @Operation(
            summary = "Chỉnh sửa nội dung một câu hỏi khảo sát",
            description = "Cập nhật nội dung, loại câu hỏi, cờ bắt buộc hoặc thứ tự hiển thị của một câu hỏi theo ID câu hỏi. Ràng buộc nghiệp vụ: Chỉ cho phép chỉnh sửa khi cuộc khảo sát chứa câu hỏi đó CHƯA có bất kỳ sinh viên nào nộp bài."
    )
    public ResponseEntity<Map<String, Object>> updateQuestion(
            @PathVariable("question-id") Integer questionId,
            @RequestBody QuestionUpdateRequest request) {

        surveyService.updateQuestion(questionId, request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Cập nhật câu hỏi khảo sát thành công.");

        return ResponseEntity.ok(response);
    }

    // API Xóa 1 câu hỏi khảo sát
    @DeleteMapping("/questions/{questionId}")
    @Operation(
            summary = "Xóa một câu hỏi khỏi cuộc khảo sát",
            description = "Xóa hoàn toàn một câu hỏi khỏi cơ sở dữ liệu dựa theo ID câu hỏi. Ràng buộc nghiệp vụ: Chỉ cho phép xóa khi cuộc khảo sát chứa câu hỏi đó CHƯA có bất kỳ sinh viên nào tham gia nộp bài nhằm bảo toàn dữ liệu thống kê."
    )
    public ResponseEntity<Map<String, Object>> deleteQuestion(
            @PathVariable("questionId") Integer questionId) { // Lưu ý sửa lại @PathVariable chính xác theo URL

        surveyService.deleteQuestion(questionId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Xóa câu hỏi khảo sát thành công.");

        return ResponseEntity.ok(response);
    }

    // API Thêm câu hỏi mới vào một cuộc khảo sát đã tồn tại
    @PostMapping("/surveys/{survey-id}/questions")
    @Operation(
            summary = "Thêm câu hỏi mới vào cuộc khảo sát đã tồn tại",
            description = "Bổ sung một câu hỏi mới vào cuộc khảo sát dựa theo ID cuộc khảo sát. Ràng buộc nghiệp vụ: Hệ thống sẽ chặn hành động này và tung lỗi nếu cuộc khảo sát này đã bắt đầu và có sinh viên tham gia nộp bài."
    )
    public ResponseEntity<Map<String, Object>> addQuestionToSurvey(
            @PathVariable("survey-id") Integer surveyId,
            @RequestBody QuestionCreateRequest request) {

        surveyService.addQuestionToSurvey(surveyId, request);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Thêm câu hỏi vào cuộc khảo sát thành công.");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}