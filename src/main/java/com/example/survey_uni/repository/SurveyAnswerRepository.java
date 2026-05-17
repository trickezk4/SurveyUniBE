package com.example.survey_uni.repository;

import com.example.survey_uni.model.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Integer> {

    // Tìm tất cả câu trả lời thuộc về một token phiên làm việc (phục vụ thống kê/đối soát ẩn danh)
    List<SurveyAnswer> findBySurveyToken(String surveyToken);
}