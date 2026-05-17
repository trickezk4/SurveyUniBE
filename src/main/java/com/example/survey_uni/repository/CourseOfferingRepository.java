package com.example.survey_uni.repository;

import com.example.survey_uni.model.CourseOffering;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseOfferingRepository extends JpaRepository<CourseOffering, Integer> {
    // Tìm tất cả các lớp học phần mà một sinh viên cụ thể đăng ký học
    @Query("SELECT co FROM CourseOffering co JOIN Enrollment e ON co.id = e.courseOffering.id WHERE e.student.id = :studentId")
    List<CourseOffering> findAllByStudentId(@Param("studentId") Integer studentId);
}