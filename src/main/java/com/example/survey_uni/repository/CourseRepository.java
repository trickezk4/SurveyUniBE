package com.example.survey_uni.repository;

import com.example.survey_uni.dto.response.CourseMinifiedResponse;
import com.example.survey_uni.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer> {

    // Truy vấn và map thẳng sang DTO rút gọn, hỗ trợ tìm kiếm theo Code hoặc Name
    @Query("SELECT new com.example.survey_uni.dto.response.CourseMinifiedResponse(c.id, c.courseCode, c.courseName) " +
            "FROM Course c " +
            "WHERE :keyword IS NULL OR " +
            "LOWER(c.courseCode) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.courseName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<CourseMinifiedResponse> searchCoursesForMenu(@Param("keyword") String keyword);
}