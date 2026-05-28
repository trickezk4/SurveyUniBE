package com.example.survey_uni.dto.request;

import com.example.survey_uni.model.SurveyQuestion;

public class QuestionCreateRequest {
    private String content;
    private SurveyQuestion.QuestionType type; // SCALE hoặc TEXT
    private Boolean isRequired;
    private Integer orderIndex;

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

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }
}
