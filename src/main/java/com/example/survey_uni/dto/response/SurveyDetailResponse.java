package com.example.survey_uni.dto.response;

import com.example.survey_uni.model.SurveyQuestion;

import java.util.List;

public class SurveyDetailResponse {

    private Integer surveyId;
    private String title;
    private String token; // Trả về token phiên làm việc hiện tại để Client giữ lại khi submit
    private List<QuestionResponse> questions;

    public SurveyDetailResponse(
            Integer surveyId,
            String title,
            String token,
            List<QuestionResponse> questions
    ) {
        this.surveyId = surveyId;
        this.title = title;
        this.token = token;
        this.questions = questions;
    }

    // Getter & Setter
    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<QuestionResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResponse> questions) {
        this.questions = questions;
    }

    // Static inner class
    public static class QuestionResponse {

        private Integer questionId;
        private String content;
        private SurveyQuestion.QuestionType type;        // SCALE hoặc TEXT
        private Boolean isRequired;
        private Integer orderIndex;

        public QuestionResponse(
                Integer questionId,
                String content,
                SurveyQuestion.QuestionType type,
                Boolean isRequired,
                Integer orderIndex
        ) {
            this.questionId = questionId;
            this.content = content;
            this.type = type;
            this.isRequired = isRequired;
            this.orderIndex = orderIndex;
        }

        // Getter & Setter
        public Integer getQuestionId() {
            return questionId;
        }

        public void setQuestionId(Integer questionId) {
            this.questionId = questionId;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public SurveyQuestion.QuestionType getType() {
            return type;
        }

        public void setType(SurveyQuestion.QuestionType type) {
            this.type = type;
        }

        public Boolean getIsRequired() {
            return isRequired;
        }

        public void setIsRequired(Boolean isRequired) {
            this.isRequired = isRequired;
        }

        public Integer getOrderIndex() {
            return orderIndex;
        }

        public void setOrderIndex(Integer orderIndex) {
            this.orderIndex = orderIndex;
        }
    }
}