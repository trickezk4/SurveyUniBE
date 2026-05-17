package com.example.survey_uni.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "surveys")
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "co_id", nullable = false)
    private CourseOffering courseOffering;

    private String title;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "start_datetime")
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime")
    private LocalDateTime endDatetime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "survey")
    private List<SurveyQuestion> questions;

    @OneToMany(mappedBy = "survey")
    private List<SurveyToken> tokens;

    @OneToMany(mappedBy = "survey")
    private List<SurveySubmission> submissions;

    public Survey() {
    }

    public Survey(Integer id, CourseOffering courseOffering, String title, Boolean isActive, LocalDateTime startDatetime, LocalDateTime endDatetime, LocalDateTime createdAt, List<SurveyQuestion> questions, List<SurveyToken> tokens, List<SurveySubmission> submissions) {
        this.id = id;
        this.courseOffering = courseOffering;
        this.title = title;
        this.isActive = isActive;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.createdAt = createdAt;
        this.questions = questions;
        this.tokens = tokens;
        this.submissions = submissions;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public CourseOffering getCourseOffering() {
        return courseOffering;
    }

    public void setCourseOffering(CourseOffering courseOffering) {
        this.courseOffering = courseOffering;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<SurveyQuestion> getQuestions() {
        return questions;
    }

    public void setQuestions(List<SurveyQuestion> questions) {
        this.questions = questions;
    }

    public List<SurveyToken> getTokens() {
        return tokens;
    }

    public void setTokens(List<SurveyToken> tokens) {
        this.tokens = tokens;
    }

    public List<SurveySubmission> getSubmissions() {
        return submissions;
    }

    public void setSubmissions(List<SurveySubmission> submissions) {
        this.submissions = submissions;
    }
}
