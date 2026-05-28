package com.example.survey_uni.service;

import com.example.survey_uni.dto.request.QuestionCreateRequest;
import com.example.survey_uni.dto.request.QuestionUpdateRequest;
import com.example.survey_uni.dto.request.SurveyCreateRequest;
import com.example.survey_uni.dto.request.SurveySubmitRequest;
import com.example.survey_uni.dto.response.SurveyDetailAdminResponse;
import com.example.survey_uni.dto.response.SurveyDetailResponse;
import com.example.survey_uni.dto.response.SurveyListResponse;
import com.example.survey_uni.dto.response.SurveyStatusResponse;
import com.example.survey_uni.model.*;
import com.example.survey_uni.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;
    @Autowired
    private SurveyTokenRepository surveyTokenRepository;
    @Autowired
    private SurveySubmissionRepository surveySubmissionRepository;
    @Autowired
    private SurveyAnswerRepository surveyAnswerRepository;
    @Autowired
    private UserRepository userRepository; // Sử dụng để map thực thể User khi tạo Token
    @Autowired
    private CourseOfferingRepository courseOfferingRepository; // Sử dụng để map thực thể User khi tạo Token

//    public SurveyService(SurveyRepository surveyRepository, SurveyQuestionRepository surveyQuestionRepository, SurveyTokenRepository surveyTokenRepository, SurveySubmissionRepository surveySubmissionRepository, SurveyAnswerRepository surveyAnswerRepository, UserRepository userRepository) {
//        this.surveyRepository = surveyRepository;
//        this.surveyQuestionRepository = surveyQuestionRepository;
//        this.surveyTokenRepository = surveyTokenRepository;
//        this.surveySubmissionRepository = surveySubmissionRepository;
//        this.surveyAnswerRepository = surveyAnswerRepository;
//        this.userRepository = userRepository;
//    }

    /**
     * 1. LẤY DANH SÁCH MÔN HỌC & TRẠNG THÁI KHẢO SÁT CHO DASHBOARD SINH VIÊN
     */
    public List<SurveyStatusResponse> getStudentDashboard(Integer studentId) {
        // Gọi câu lệnh Query đã viết ở Repository để lấy mảng Object dữ liệu thô
        List<Object[]> rawData = surveyRepository.getSurveyStatusDashboard(studentId);
        List<SurveyStatusResponse> responseList = new ArrayList<>();

        for (Object[] row : rawData) {
            SurveyStatusResponse dto = new SurveyStatusResponse();
            dto.setCoId((Integer) row[0]);
            dto.setCourseCode((String) row[1]);
            dto.setCourseName((String) row[2]);
            dto.setLecturerName((String) row[3]);
            dto.setSurveyId((Integer) row[4]);
            dto.setSurveyStatus((String) row[5]);
            responseList.add(dto);
        }
        return responseList;
    }

    /**
     * 2. BẮT ĐẦU KHẢO SÁT: KIỂM TRA & KHỞI TẠO PHIÊN LÀM VIỆC (TOKEN)
     */
    @Transactional
    public SurveyDetailResponse startSurvey(Integer surveyId, Integer studentId) {
        // Kiểm tra cuộc khảo sát có tồn tại và đang hoạt động không
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc khảo sát này hoặc khảo sát đã bị đóng."));

        if (!survey.getActive()) {
            throw new RuntimeException("Cuộc khảo sát này hiện không còn hoạt động.");
        }

        // Kiểm tra xem sinh viên này đã từng nộp bài cho khảo sát này chưa (Chặn nộp trùng)
        boolean alreadySubmitted = surveySubmissionRepository.existsBySurveyIdAndStudentUserId(surveyId, studentId);
        if (alreadySubmitted) {
            throw new RuntimeException("Bạn đã hoàn thành cuộc khảo sát này trước đó rồi.");
        }

        // Kiểm tra xem sinh viên đã có token nào đang làm dở (chưa nộp) cho khảo sát này không
        SurveyToken surveyToken = surveyTokenRepository
                .findBySurveyIdAndStudentUserIdAndIsSubmittedFalse(surveyId, studentId)
                .orElseGet(() -> {
                    // Nếu chưa có, tiến hành sinh một mã token số ngẫu nhiên đơn giản (rút gọn từ UUID)
                    User student = userRepository.findById(studentId)
                            .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin sinh viên."));

                    SurveyToken newToken = new SurveyToken();
                    newToken.setSurvey(survey);
                    newToken.setStudent(student);
                    newToken.setSubmitted(false);
                    newToken.setCreatedAt(LocalDateTime.now());

                    // Để đơn giản thay cho UUID, ta sinh chuỗi số ngẫu nhiên kèm timestamp bảo đảm tính duy nhất
                    String simpleTokenStr = "TK-" + System.currentTimeMillis() + "-" + (1000 + new Random().nextInt(9000));
                    newToken.setToken(simpleTokenStr);

                    return surveyTokenRepository.save(newToken);
                });

        // Lấy danh sách câu hỏi sắp xếp theo đúng thứ tự thiết lập
        List<SurveyQuestion> questions = surveyQuestionRepository.findBySurveyIdOrderByOrderIndexAsc(surveyId);

        // Map sang DTO để trả về cho Client hiển thị lên giao diện bài làm
        List<SurveyDetailResponse.QuestionResponse> questionResponses = questions.stream().map(q ->
                new SurveyDetailResponse.QuestionResponse(q.getId(), q.getContent(), q.getType(), q.getRequired(), q.getOrderIndex())
        ).collect(Collectors.toList());

        return new SurveyDetailResponse(survey.getId(), survey.getTitle(), surveyToken.getToken(), questionResponses);
    }

    /**
     * 3. NỘP BÀI KHẢO SÁT: TRANSACTION BẢO ĐẢM TÍNH ẨN DANH TUYỆT ĐỐI
     */
    @Transactional
    public void submitSurvey(SurveySubmitRequest request, Integer studentId) {
        // Bước A: Xác thực Token truyền lên từ Client
        SurveyToken surveyToken = surveyTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Mã phiên làm việc (Token) không hợp lệ."));

        if (surveyToken.getSubmitted()) {
            throw new RuntimeException("Phiên làm việc này đã được nộp và khóa lại trước đó.");
        }

        // Đảm bảo token này thuộc đúng sinh viên đang thực hiện lệnh gọi API
        if (surveyToken.getStudent() == null || !surveyToken.getStudent().getUserId().equals(studentId)) {
            throw new RuntimeException("Bạn không có quyền thao tác trên phiên làm việc này.");
        }

        // --- VALIDATION CÂU HỎI BẮT BUỘC ---
        // 1. Lấy toàn bộ bộ câu hỏi của cuộc khảo sát này từ Database
        List<SurveyQuestion> dbQuestions = surveyQuestionRepository.findBySurveyIdOrderByOrderIndexAsc(request.getSurveyId());

        // 2. Duyệt qua từng câu hỏi trong DB để ép kiểm tra điều kiện bắt buộc
        for (SurveyQuestion dbQ : dbQuestions) {
            if (dbQ.getRequired()) {

                // Tìm câu trả lời tương ứng mà sinh viên gửi lên dựa theo ID câu hỏi trong DB
                SurveySubmitRequest.AnswerRequest studentAns = null;
                if (request.getAnswers() != null) {
                    studentAns = request.getAnswers().stream()
                            .filter(ans -> ans.getQuestionId().equals(dbQ.getId()))
                            .findFirst()
                            .orElse(null);
                }

                // Trường hợp 1: Client không gửi câu hỏi này lên (Mảng answers thiếu phần tử)
                if (studentAns == null) {
                    throw new RuntimeException("Thiếu dữ liệu câu hỏi bắt buộc: " + dbQ.getContent());
                }

                // Trường hợp 2: Có gửi lên nhưng nội dung bên trong trống rỗng (Cả 2 trường score và value đều null/trống)
                boolean isScoreEmpty = (studentAns.getAnswerScore() == null);
                boolean isValueEmpty = (studentAns.getAnswerValue() == null || studentAns.getAnswerValue().trim().isEmpty());

                if (isScoreEmpty && isValueEmpty) {
                    throw new RuntimeException("Câu hỏi bắt buộc không được để trống: " + dbQ.getContent());
                }
            }
        }

        // Bước B: Lưu toàn bộ câu trả lời của sinh viên vào [LỚP ẨN DANH]
        for (SurveySubmitRequest.AnswerRequest ansReq : request.getAnswers()) {
            SurveyQuestion question = surveyQuestionRepository.findById(ansReq.getQuestionId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy câu hỏi với ID: " + ansReq.getQuestionId()));

            SurveyAnswer answer = new SurveyAnswer();
            answer.setSurveyToken(surveyToken.getToken()); // Liên kết lỏng bằng chuỗi token, không liên kết thực thể User
            answer.setQuestion(question);
            answer.setAnswerScore(ansReq.getAnswerScore());
            answer.setAnswerValue(ansReq.getAnswerValue());
            answer.setCreatedAt(LocalDateTime.now());

            surveyAnswerRepository.save(answer);
        }

        // Bước C: Ghi nhận thông tin vào [LỚP ĐỊNH DANH] để khóa trạng thái, không cho làm lại
        SurveySubmission submission = new SurveySubmission();
        submission.setSurvey(surveyToken.getSurvey());
        submission.setStudent(surveyToken.getStudent());
        submission.setStatus(SurveySubmission.SubmissionStatus.SUBMITTED);
        submission.setSubmittedAt(LocalDateTime.now());

        surveySubmissionRepository.save(submission);

        // Bước D: Ẩn danh hóa phiên làm việc (Ngắt liên kết tài khoản khỏi Token)
        surveyToken.setStudent(null); // Đưa thông tin định danh về NULL để không ai dò lại được câu trả lời
        surveyToken.setSubmitted(true);
        surveyToken.setDeletedAt(LocalDateTime.now()); // Đánh dấu soft-delete audit phiên

        surveyTokenRepository.save(surveyToken);
    }

    /**
     * 4. [ADMIN] TẠO MỚI KHẢO SÁT VÀ CÂU HỎI CÙNG LÚC
     */
    @Transactional
    public Integer createSurveyWithQuestions(SurveyCreateRequest request) {
        // 1. Kiểm tra Lớp học phần có tồn tại không
        CourseOffering courseOffering = courseOfferingRepository.findById(request.getCoId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy lớp học phần với ID: " + request.getCoId()));

        // 2. Kiểm tra lớp này đã có khảo sát chưa
        if (surveyRepository.findByCourseOfferingId(request.getCoId()).isPresent()) {
            throw new RuntimeException("Lớp học phần này đã được thiết lập khảo sát trước đó.");
        }

        // 3. Ràng buộc logic thời gian
        if (request.getStartDatetime().isAfter(request.getEndDatetime())) {
            throw new RuntimeException("Thời gian bắt đầu không được lớn hơn thời gian kết thúc.");
        }

        // 4. Khởi tạo object Survey
        Survey survey = new Survey();
        survey.setCourseOffering(courseOffering);
        survey.setTitle(request.getTitle());
        survey.setStartDatetime(request.getStartDatetime());
        survey.setEndDatetime(request.getEndDatetime());
        survey.setActive(true); // Mặc định mở để tiện test
        survey.setCreatedAt(LocalDateTime.now());

        // 5. Duyệt và khởi tạo danh sách Câu hỏi
        List<SurveyQuestion> questions = new ArrayList<>();
        if (request.getQuestions() == null || request.getQuestions().isEmpty()) {
            throw new RuntimeException("Khảo sát phải có ít nhất 1 câu hỏi.");
        }

        for (SurveyCreateRequest.QuestionRequest qReq : request.getQuestions()) {
            SurveyQuestion question = new SurveyQuestion();
            question.setSurvey(survey); // Gắn reference ngược lại Survey
            question.setContent(qReq.getContent());
            question.setType(qReq.getType());
            question.setRequired(qReq.getIsRequired());
            question.setOrderIndex(qReq.getOrderIndex());
            questions.add(question);
        }

        // Gắn danh sách câu hỏi vào Survey.
        // Nhờ cấu hình CascadeType.ALL ở Entity, khi save Survey nó sẽ tự động save toàn bộ Question.
        survey.setQuestions(questions);

        // 6. Lưu vào Database
        Survey savedSurvey = surveyRepository.save(survey);
        return savedSurvey.getId(); // Trả về ID để Controller phản hồi cho Client
    }

    /**
     * 5. [ADMIN] LẤY DANH SÁCH TẤT CẢ KHẢO SÁT
     */
    public List<SurveyListResponse> getAllSurveys() {
        List<Survey> surveys = surveyRepository.findAllSurveysWithDetails();

        return surveys.stream().map(
                s -> new SurveyListResponse(
                        s.getId(),
                        s.getTitle(),
                        s.getCourseOffering().getCourse().getCourseCode(),
                        s.getCourseOffering().getCourse().getCourseName(),
                        s.getCourseOffering().getGroupCode(),
                        s.getCourseOffering().getLecturer().getUsername(),
                        s.getStartDatetime(), s.getEndDatetime(),
                        s.getActive())
                ).collect(Collectors.toList());
    }

    /**
     * 6. [ADMIN] LẤY CHI TIẾT 1 CUỘC KHẢO SÁT THEO ID
     */
    public SurveyDetailAdminResponse getSurveyDetail(Integer id) {
        Survey s = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc khảo sát với ID: " + id));

        // Map danh sách câu hỏi entity sang DTO con
        List<SurveyDetailAdminResponse.QuestionResponse> questionResponses = s.getQuestions().stream()
                .map(q -> new SurveyDetailAdminResponse.QuestionResponse(
                        q.getId(),
                        q.getContent(),
                        q.getType(),
                        q.getRequired(),
                        q.getOrderIndex()
                )).collect(Collectors.toList());

        // Build Response tổng thể
        return new SurveyDetailAdminResponse(
                s.getId(),
                s.getTitle(),
                s.getCourseOffering().getCourse().getCourseCode(),
                s.getCourseOffering().getCourse().getCourseName(),
                s.getCourseOffering().getGroupCode(),
                s.getCourseOffering().getLecturer().getUsername(),
                s.getStartDatetime(),
                s.getEndDatetime(),
                s.getActive(),
                questionResponses
        );
    }

    /**
     * 7. [ADMIN] THAY ĐỔI TRẠNG THÁI ĐÓNG/MỜ KHẢO SÁT THỦ CÔNG
     */
    @Transactional
    public void updateSurveyStatus(Integer id, Boolean isActive) {
        // 1. Kiểm tra cuộc khảo sát có tồn tại không
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc khảo sát với ID: " + id));

        // 2. Cập nhật trạng thái mới
        survey.setActive(isActive);

        // 3. Lưu lại vào DB
        surveyRepository.save(survey);
    }

    /**
     * 8. [ADMIN] CHỈNH SỬA MỘT CÂU HỎI KHẢO SÁT
     */
    @Transactional
    public void updateQuestion(Integer questionId, QuestionUpdateRequest request) {
        // 1. Tìm câu hỏi
        SurveyQuestion question = surveyQuestionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy câu hỏi với ID: " + questionId));

        // 2. Kiểm tra nghiệp vụ => Khảo sát chỉ được sửa khi chưa có ai làm bài
        Survey survey = question.getSurvey();
        if (survey.getSubmissions() != null && !survey.getSubmissions().isEmpty()) {
            throw new RuntimeException("Không thể chỉnh sửa câu hỏi vì cuộc khảo sát này đã có sinh viên nộp bài.");
        }

        // 3. Cập nhật thông tin khảo sát mới
        question.setContent(request.getContent());
        question.setType(request.getType());
        question.setRequired(request.getRequired());
        question.setOrderIndex(request.getOrderIndex());

        // 4. Lưu lại
        surveyQuestionRepository.save(question);
    }

    /**
     * 9. [ADMIN] XÓA MỘT CÂU HỎI KHẢO SÁT
     */
    @Transactional
    public void deleteQuestion(Integer questionId) {
        // 1. Tìm câu hỏi
        SurveyQuestion question = surveyQuestionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy câu hỏi với ID: " + questionId));

        // 2. Kiểm tra nghiệp vụ tương tự như sửa
        Survey survey = question.getSurvey();
        if (survey.getSubmissions() != null && !survey.getSubmissions().isEmpty()) {
            throw new RuntimeException("Không thể xóa câu hỏi vì cuộc khảo sát này đã có sinh viên nộp bài.");
        }

        // 3. Gỡ bỏ liên kết trong mảng của đối tượng Cha (Survey) để Hibernate đồng bộ bộ nhớ cache
        survey.getQuestions().remove(question);

        // 4. Thực hiện xóa dưới DB
        surveyQuestionRepository.delete(question);
    }

    /**
     * 10. [ADMIN] THÊM CÂU HỎI MỚI VÀO CUỘC KHẢO SÁT ĐÃ TỒN TẠI
     */
    @Transactional
    public void addQuestionToSurvey(Integer surveyId, QuestionCreateRequest request) {
        // 1. Tìm cuộc khảo sát xem có tồn tại không
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy cuộc khảo sát với ID: " + surveyId));

        // 2. Kiểm tra nghiệp vụ tương tự như sửa
        if (survey.getSubmissions() != null && !survey.getSubmissions().isEmpty()) {
            throw new RuntimeException("Không thể thêm câu hỏi vì cuộc khảo sát này đã có sinh viên nộp bài.");
        }

        // 3. Khởi tạo đối tượng câu hỏi mới và thiết lập mối quan hệ
        SurveyQuestion newQuestion = new SurveyQuestion();
        newQuestion.setSurvey(survey); // Gắn câu hỏi vào khảo sát cha
        newQuestion.setContent(request.getContent());
        newQuestion.setType(request.getType());
        newQuestion.setRequired(request.getRequired());
        newQuestion.setOrderIndex(request.getOrderIndex());

        // 4. Lưu xuống bảng survey_questions
        surveyQuestionRepository.save(newQuestion);
    }
}