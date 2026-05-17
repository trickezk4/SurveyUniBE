package com.example.survey_uni.repository;

import com.example.survey_uni.model.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Integer> {

    // Tìm khảo sát đang kích hoạt của một lớp học phần
    Optional<Survey> findByCourseOfferingIdAndIsActiveTrue(Integer coId);

    // Truy vấn chức năng "Xem danh sách môn học & trạng thái" của sinh viên
    @Query(value = "SELECT co.co_id as coId, c.course_code as courseCode, c.course_name as courseName, " +
            "u.user_name as lecturerName, s.survey_id as surveyId, " +
            "CASE " +
            "  WHEN sub.submission_id IS NOT NULL THEN 'HOAN_THANH' " +
            "  WHEN tk.stoken_id IS NOT NULL AND tk.is_submitted = false THEN 'DANG_THUC_HIEN' " +
            "  ELSE 'CHUA_BAT_DAU' " +
            "END as surveyStatus " +
            "FROM enrollments e " +
            "JOIN course_offering co ON e.co_id = co.co_id " +
            "JOIN course c ON co.course_id = c.course_id " +
            "LEFT JOIN users u ON co.lecturer_id = u.user_id " +
            "LEFT JOIN surveys s ON co.co_id = s.co_id AND s.is_active = true " +
            "LEFT JOIN survey_submissions sub ON s.survey_id = sub.survey_id AND sub.student_id = e.student_id " +
            "LEFT JOIN survey_tokens tk ON s.survey_id = tk.survey_id AND tk.student_id = e.student_id " +
            "WHERE e.student_id = :studentId", nativeQuery = true)
    List<Object[]> getSurveyStatusDashboard(@Param("studentId") Integer studentId);
}