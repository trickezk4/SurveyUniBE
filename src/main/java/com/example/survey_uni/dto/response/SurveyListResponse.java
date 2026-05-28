package com.example.survey_uni.dto.response;

import java.time.LocalDateTime;

public class SurveyListResponse {
    private Integer id;
    private String title;
    private String courseCode;
    private String courseName;
    private String groupCode;
    private String lecturerName;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private Boolean isActive;

    public SurveyListResponse(){

    }

    public SurveyListResponse(Integer id, String title, String courseCode, String courseName, String groupCode, String lecturerName, LocalDateTime startDatetime, LocalDateTime endDatetime, Boolean isActive) {
        this.id = id;
        this.title = title;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.groupCode = groupCode;
        this.lecturerName = lecturerName;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.isActive = isActive;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
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

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }
}
