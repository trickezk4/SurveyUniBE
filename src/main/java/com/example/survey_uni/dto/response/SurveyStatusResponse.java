package com.example.survey_uni.dto.response;

public class SurveyStatusResponse {

    private Integer coId;
    private String courseCode;
    private String courseName;
    private String lecturerName;
    private Integer surveyId;       // Có thể null nếu lớp chưa mở khảo sát
    private String surveyStatus;    // CHUA_BAT_DAU, DANG_THUC_HIEN, HOAN_THANH

    public SurveyStatusResponse() {

    }

    // Getter & Setter
    public Integer getCoId() {
        return coId;
    }

    public void setCoId(Integer coId) {
        this.coId = coId;
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

    public String getLecturerName() {
        return lecturerName;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public Integer getSurveyId() {
        return surveyId;
    }

    public void setSurveyId(Integer surveyId) {
        this.surveyId = surveyId;
    }

    public String getSurveyStatus() {
        return surveyStatus;
    }

    public void setSurveyStatus(String surveyStatus) {
        this.surveyStatus = surveyStatus;
    }
}