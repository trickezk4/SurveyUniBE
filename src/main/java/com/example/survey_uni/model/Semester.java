package com.example.survey_uni.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "semester")
public class Semester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "semester_id")
    private Integer id;

    @Column(name = "semester_name", nullable = false)
    private String semesterName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "semester_status")
    private SemesterStatus semesterStatus;

    @OneToMany(mappedBy = "semester")
    private List<CourseOffering> offerings;

    public enum SemesterStatus {
        ACTIVE,
        INACTIVE
    }

    public Semester(){

    }

    public Semester(Integer id, String semesterName, LocalDate startDate, LocalDate endDate, SemesterStatus semesterStatus, List<CourseOffering> offerings) {
        this.id = id;
        this.semesterName = semesterName;
        this.startDate = startDate;
        this.endDate = endDate;
        this.semesterStatus = semesterStatus;
        this.offerings = offerings;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getSemesterName() {
        return semesterName;
    }

    public void setSemesterName(String semesterName) {
        this.semesterName = semesterName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public SemesterStatus getSemesterStatus() {
        return semesterStatus;
    }

    public void setSemesterStatus(SemesterStatus semesterStatus) {
        this.semesterStatus = semesterStatus;
    }

    public List<CourseOffering> getOfferings() {
        return offerings;
    }

    public void setOfferings(List<CourseOffering> offerings) {
        this.offerings = offerings;
    }
}
