package com.example.survey_uni.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "survey_questions")
public class SurveyQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    @Column(name = "is_required")
    private Boolean isRequired;

    @Column(name = "order_index")
    private Integer orderIndex;

    @OneToMany(mappedBy = "question")
    private List<SurveyAnswer> answers;

    public enum QuestionType {
        SCALE,
        TEXT
    }

    public SurveyQuestion() {

    }

    public SurveyQuestion(Integer id, Survey survey, String content, QuestionType type, Boolean isRequired, Integer orderIndex, List<SurveyAnswer> answers) {
        this.id = id;
        this.survey = survey;
        this.content = content;
        this.type = type;
        this.isRequired = isRequired;
        this.orderIndex = orderIndex;
        this.answers = answers;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public QuestionType getType() {
        return type;
    }

    public void setType(QuestionType type) {
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

    public List<SurveyAnswer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<SurveyAnswer> answers) {
        this.answers = answers;
    }
}