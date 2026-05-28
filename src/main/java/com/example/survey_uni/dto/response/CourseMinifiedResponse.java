package com.example.survey_uni.dto.response;


import com.example.survey_uni.model.Course;

public class CourseMinifiedResponse {
    private Integer courseId;
    private String courseCode;
    private String courseName;

    public CourseMinifiedResponse() {

    }

    public CourseMinifiedResponse(Integer courseId, String courseCode, String courseName) {
        this.courseId = courseId;
        this.courseCode = courseCode;
        this.courseName = courseName;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
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
}