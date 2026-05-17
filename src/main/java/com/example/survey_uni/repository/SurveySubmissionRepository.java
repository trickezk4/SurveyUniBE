package com.example.survey_uni.repository;

import com.example.survey_uni.model.SurveySubmission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveySubmissionRepository extends JpaRepository<SurveySubmission, Integer> {

    // Kiểm tra nhanh xem sinh viên đã nộp bài cho khảo sát này chưa
    boolean existsBySurveyIdAndStudentUserId(Integer surveyId, Integer studentId);
}