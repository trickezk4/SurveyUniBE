package com.example.survey_uni.repository;

import com.example.survey_uni.dto.response.ClassOfferingMinifiedResponse;
import com.example.survey_uni.model.CourseOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseOfferingRepository extends JpaRepository<CourseOffering, Integer> {
    // Tìm tất cả các lớp học phần mà một sinh viên cụ thể đăng ký học
    @Query("SELECT co FROM CourseOffering co JOIN Enrollment e ON co.id = e.courseOffering.id WHERE e.student.id = :studentId")
    List<CourseOffering> findAllByStudentId(@Param("studentId") Integer studentId);

    @Query("SELECT new com.example.survey_uni.dto.response.ClassOfferingMinifiedResponse(co.id, co.groupCode, l.username) " +
            "FROM CourseOffering co " +
            "JOIN co.course c " +
            "JOIN co.semester s " +
            "JOIN co.lecturer l " + // Đã sửa thành co.lecturer khớp chuẩn với Entity của bạn
            "WHERE c.id = :courseId " +
            "AND s.semesterStatus = 'ACTIVE' " /* +
            "AND NOT EXISTS (SELECT 1 FROM Survey svy WHERE svy.courseOffering = co)" */ // check lớp học phần đã khảo sát hay chưa => đề xuất chỉ khảo sát một lần thì thêm dòng này
            )
    List<ClassOfferingMinifiedResponse> findActiveOfferingsByCourseId(@Param("courseId") Integer courseId);
}