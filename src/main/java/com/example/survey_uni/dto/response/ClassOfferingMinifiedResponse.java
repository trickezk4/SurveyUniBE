package com.example.survey_uni.dto.response;

public class ClassOfferingMinifiedResponse {
    private Integer coId;
    private String groupCode;
    private String lecturerName;

    public ClassOfferingMinifiedResponse(){

    }

    public ClassOfferingMinifiedResponse(Integer coId, String groupCode, String lecturerName) {
        this.coId = coId;
        this.groupCode = groupCode;
        this.lecturerName = lecturerName;
    }

    public Integer getCoId() {
        return coId;
    }

    public void setCoId(Integer coId) {
        this.coId = coId;
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
}
