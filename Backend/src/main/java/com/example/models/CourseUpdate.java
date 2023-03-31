package com.example.models;

public class CourseUpdate {
    private CourseUpdateActions action;
    private String courseId;

    public CourseUpdateActions getAction() {
        return action;
    }

    public void setAction(CourseUpdateActions action) {
        this.action = action;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public enum CourseUpdateActions {
        ADD,
        REMOVE
    }

}
