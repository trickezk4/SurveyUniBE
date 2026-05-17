package com.example.survey_uni.controller;

import com.example.survey_uni.dto.request.SurveySubmitRequest;
import com.example.survey_uni.dto.response.SurveyDetailResponse;
import com.example.survey_uni.dto.response.SurveyStatusResponse;
import com.example.survey_uni.model.User;
import com.example.survey_uni.repository.UserRepository;
import com.example.survey_uni.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private UserRepository userRepository;
    /**
     * LƯU Ý QUAN TRỌNG VỀ JWT:
     * Hàm helper này dùng để lấy studentId từ đối tượng Authentication của Spring Security.
     * Bạn hãy điều chỉnh đoạn ép kiểu (Cast) dưới đây cho khớp với Object Principal
     * mà bạn đã lưu vào SecurityContext tại lớp `JwtFilter` của bạn.
     */
    private Integer getAuthenticatedStudentId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Người dùng chưa được xác thực.");
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String email = userDetails.getUsername(); // Lấy email từ UserDetails

            // Tìm User trong DB bằng email để lấy ra user_id (Integer)
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy tài khoản sinh viên hợp lệ."));

            // Bạn hãy kiểm tra xem trong Entity User.java bạn đặt tên hàm getter là getId() hay getUserId() nhé
            return user.getUserId();
        }

        throw new RuntimeException("Cấu trúc Principal không hợp lệ.");
    }
//    private Integer getAuthenticatedStudentId(Authentication authentication) {
//        if (authentication == null || !authentication.isAuthenticated()) {
//            throw new RuntimeException("Người dùng chưa được xác thực.");
//        }
//
//        // Lấy ra email sinh viên từ UserDetails
//        String email = authentication.getName();
//
//        // Truy vấn xuống DB để lấy ID thực tế
//        return userRepository.findByEmail(email)
//                .map(user -> user.getUserId()) // Giả định Entity User của bạn có hàm getId()
//                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên với email: " + email));
//    }

    /**
     * 1. API XEM DANH SÁCH MÔN HỌC & TRẠNG THÁI KHẢO SÁT
     * GET: /api/v1/surveys/my-courses
     */
    @GetMapping("/my-courses")
    public ResponseEntity<Map<String, Object>> getMyCoursesDashboard(Authentication authentication) {
        Integer studentId = getAuthenticatedStudentId(authentication);
        List<SurveyStatusResponse> dashboard = surveyService.getStudentDashboard(studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", dashboard);

        return ResponseEntity.ok(response);
    }

    /**
     * 2. API BẮT ĐẦU LÀM KHẢO SÁT (LẤY ĐỀ & SINH TOKEN)
     * POST: /api/v1/surveys/{surveyId}/start
     */
    @PostMapping("/{surveyId}/start")
    public ResponseEntity<Map<String, Object>> startSurvey(
            @PathVariable Integer surveyId,
            Authentication authentication) {

        // THÊM DÒNG NÀY ĐỂ DEBUG:
        System.out.println("====> ID khảo sát nhận được từ Postman: " + surveyId);

        Integer studentId = getAuthenticatedStudentId(authentication);
        SurveyDetailResponse surveyDetail = surveyService.startSurvey(surveyId, studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", surveyDetail);

        return ResponseEntity.ok(response);
    }

    /**
     * 3. API NỘP BÀI KHẢO SÁT (ẨN DANH HÓA)
     * POST: /api/v1/surveys/submit
     */
    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitSurvey(
            @RequestBody SurveySubmitRequest request,
            Authentication authentication) {

        Integer studentId = getAuthenticatedStudentId(authentication);
        surveyService.submitSurvey(request, studentId);

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Khảo sát của bạn đã được ghi nhận ẩn danh thành công.");

        return ResponseEntity.ok(response);
    }
}