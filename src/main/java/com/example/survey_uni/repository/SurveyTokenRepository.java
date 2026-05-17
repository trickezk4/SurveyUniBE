package com.example.survey_uni.repository;

import com.example.survey_uni.model.SurveyToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SurveyTokenRepository extends JpaRepository<SurveyToken, Integer> {

    // Tìm phiên làm việc dựa vào chuỗi token gửi lên từ client
    Optional<SurveyToken> findByToken(String token);

    // Kiểm tra sinh viên đã có token hoạt động (chưa nộp) cho khảo sát này chưa
    Optional<SurveyToken> findBySurveyIdAndStudentUserIdAndIsSubmittedFalse(Integer surveyId, Integer studentId);
}