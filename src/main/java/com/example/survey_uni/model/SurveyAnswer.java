package com.example.survey_uni.model;

import com.example.survey_uni.model.SurveyQuestion;
import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "survey_answers")

public class SurveyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Integer id;

    @Column(name = "survey_token", nullable = false)
    private String surveyToken;

    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private SurveyQuestion question;

    @Column(name = "answer_value", columnDefinition = "TEXT")
    private String answerValue;

    @Column(name = "answer_score")
    private Integer answerScore;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public SurveyAnswer() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSurveyToken() {
        return surveyToken;
    }

    public void setSurveyToken(String surveyToken) {
        this.surveyToken = surveyToken;
    }

    public SurveyQuestion getQuestion() {
        return question;
    }

    public void setQuestion(SurveyQuestion question) {
        this.question = question;
    }

    public String getAnswerValue() {
        return answerValue;
    }

    public void setAnswerValue(String answerValue) {
        this.answerValue = answerValue;
    }

    public Integer getAnswerScore() {
        return answerScore;
    }

    public void setAnswerScore(Integer answerScore) {
        this.answerScore = answerScore;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}