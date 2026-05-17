package com.example.survey_uni.model;

import com.example.survey_uni.model.User;
import jakarta.persistence.*;

@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {
                        "student_id",
                        "co_id"
                })
        }
)
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enroll_id")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private User student;

    @ManyToOne
    @JoinColumn(name = "co_id")
    private CourseOffering courseOffering;

    public Enrollment(){

    }

    public Enrollment(Integer id, User student, CourseOffering courseOffering) {
        this.id = id;
        this.student = student;
        this.courseOffering = courseOffering;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public CourseOffering getCourseOffering() {
        return courseOffering;
    }

    public void setCourseOffering(CourseOffering courseOffering) {
        this.courseOffering = courseOffering;
    }
}