package com.example.survey_uni.dto.request;

import java.util.List;

public class SurveySubmitRequest {

    private String token; // Chuỗi Token định danh phiên làm việc ẩn danh
    private Integer surveyId;
    private List<AnswerRequest> answers;

    public SurveySubmitRequest(
            String token,
            Integer surveyId,
            List<AnswerRequest> answers
    ) {
        this.token = token;
        this.surveyId = surveyId;
        this.answers = answers;
    }

    // Getter & Setter
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public List<AnswerRequest> getAnswers() {
        return answers;
    }

    public void setAnswers(List<AnswerRequest> answers) {
        this.answers = answers;
    }

    // Static inner class
    public static class AnswerRequest {

        private Integer questionId;
        private Integer answerScore; // Có thể null nếu type = TEXT
        private String answerValue;  // Có thể null nếu type = SCALE

        public AnswerRequest(
                Integer questionId,
                Integer answerScore,
                String answerValue
        ) {
            this.questionId = questionId;
            this.answerScore = answerScore;
            this.answerValue = answerValue;
        }

        // Getter & Setter
        public Integer getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Integer questionId) {
            this.questionId = questionId;
        }

        public Integer getAnswerScore() {
            return answerScore;
        }

        public void setAnswerScore(Integer answerScore) {
            this.answerScore = answerScore;
        }

        public String getAnswerValue() {
            return answerValue;
        }

        public void setAnswerValue(String answerValue) {
            this.answerValue = answerValue;
        }
    }
}