package com.example.survey_uni.dto.request;

import java.time.LocalDateTime;
import java.util.List;

public class SurveyCreateRequest {

    private Integer coId; // Khớp với co_id trong course_offering
    private String title;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private List<QuestionRequest> questions;

    // All-args constructor
    public SurveyCreateRequest(
            Integer coId,
            String title,
            LocalDateTime startDatetime,
            LocalDateTime endDatetime,
            List<QuestionRequest> questions
    ) {
        this.coId = coId;
        this.title = title;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.questions = questions;
    }

    // Getter & Setter
    public Integer getCoId() {
        return coId;
    }

    public void setCoId(Integer coId) {
        this.coId = coId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartDatetime() {
        return startDatetime;
    }

    public void setStartDatetime(LocalDateTime startDatetime) {
        this.startDatetime = startDatetime;
    }

    public LocalDateTime getEndDatetime() {
        return endDatetime;
    }

    public void setEndDatetime(LocalDateTime endDatetime) {
        this.endDatetime = endDatetime;
    }

    public List<QuestionRequest> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionRequest> questions) {
        this.questions = questions;
    }

    // Static inner class
    public static class QuestionRequest {

        private String content;
        private String type; // SCALE hoặc TEXT (Enum trong DB)
        private Boolean isRequired;
        private Integer orderIndex;


        public QuestionRequest(
                String content,
                String type,
                Boolean isRequired,
                Integer orderIndex
        ) {
            this.content = content;
            this.type = type;
            this.isRequired = isRequired;
            this.orderIndex = orderIndex;
        }

        // Getter & Setter
        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
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