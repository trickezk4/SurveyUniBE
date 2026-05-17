package com.example.survey_uni.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(
        name = "course_offering",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "course_id",
                        "semester_id",
                        "group_code"
                })
        }
)
public class CourseOffering {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "co_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne
    @JoinColumn(name = "semester_id", nullable = false)
    private Semester semester;

    // users table
    @ManyToOne
    @JoinColumn(name = "lecturer_id")
    private User lecturer;

    @Column(name = "group_code")
    private String groupCode;

    @OneToMany(mappedBy = "courseOffering")
    private List<Enrollment> enrollments;

    @OneToMany(mappedBy = "courseOffering")
    private List<Survey> surveys;

    public CourseOffering() {

    }

    public CourseOffering(Integer id, Course course, Semester semester, User lecturer, String groupCode, List<Enrollment> enrollments, List<Survey> surveys) {
        this.id = id;
        this.course = course;
        this.semester = semester;
        this.lecturer = lecturer;
        this.groupCode = groupCode;
        this.enrollments = enrollments;
        this.surveys = surveys;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public User getLecturer() {
        return lecturer;
    }

    public void setLecturer(User lecturer) {
        this.lecturer = lecturer;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public List<Enrollment> getEnrollments() {
        return enrollments;
    }

    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }

    public List<Survey> getSurveys() {
        return surveys;
    }

    public void setSurveys(List<Survey> surveys) {
        this.surveys = surveys;
    }
}