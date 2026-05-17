package com.example.survey_uni.repository;

import com.example.survey_uni.model.SurveyQuestion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestion, Integer> {

    // Lấy danh sách câu hỏi và tự động sắp xếp theo order_index tăng dần
    List<SurveyQuestion> findBySurveyIdOrderByOrderIndexAsc(Integer surveyId);
}