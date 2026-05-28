package com.example.survey_uni.service;

import com.example.survey_uni.dto.response.ClassOfferingMinifiedResponse;
import com.example.survey_uni.dto.response.CourseMinifiedResponse;
import com.example.survey_uni.repository.CourseOfferingRepository;
import com.example.survey_uni.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private CourseOfferingRepository courseOfferingRepository;

    public List<CourseMinifiedResponse> getCoursesForMenu(String keyword) {
        // Nếu keyword truyền lên bị trống hoặc chỉ toàn dấu cách, ta coi như là lấy toàn bộ (null)
        String cleanKeyword = (keyword != null && !keyword.trim().isEmpty()) ? keyword.trim() : null;
        return courseRepository.searchCoursesForMenu(cleanKeyword);
    }


    public List<ClassOfferingMinifiedResponse> getClassOfferingsByCourse(Integer courseId) {
        if (courseId == null) {
            return Collections.emptyList();
        }
        return courseOfferingRepository.findActiveOfferingsByCourseId(courseId);
    }
}