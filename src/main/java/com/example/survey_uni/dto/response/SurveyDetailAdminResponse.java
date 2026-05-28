package com.example.survey_uni.dto.response;

import com.example.survey_uni.model.SurveyQuestion;

import java.time.LocalDateTime;
import java.util.List;

public class SurveyDetailAdminResponse {
    private Integer id;
    private String title;
    private String courseCode;
    private String courseName;
    private String groupCode;
    private String lecturerName;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private Boolean isActive;
    private List<QuestionResponse> questions;

    public SurveyDetailAdminResponse(){}

    public SurveyDetailAdminResponse(Integer id, String title, String courseCode, String courseName, String groupCode, String lecturerName, LocalDateTime startDatetime, LocalDateTime endDatetime, Boolean isActive, List<QuestionResponse> questions) {
        this.id = id;
        this.title = title;
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.groupCode = groupCode;
        this.lecturerName = lecturerName;
        this.startDatetime = startDatetime;
        this.endDatetime = endDatetime;
        this.isActive = isActive;
        this.questions = questions;
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

    public List<QuestionResponse> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionResponse> questions) {
        this.questions = questions;
    }

    public static class QuestionResponse {
        private Integer id;
        private String content;
        private SurveyQuestion.QuestionType type;
        private Boolean isRequired;
        private Integer orderIndex;

        public QuestionResponse(){}

        public QuestionResponse(Integer id, String content, SurveyQuestion.QuestionType type, Boolean isRequired, Integer orderIndex) {
            this.id = id;
            this.content = content;
            this.type = type;
            this.isRequired = isRequired;
            this.orderIndex = orderIndex;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
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
}
