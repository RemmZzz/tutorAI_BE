package com.tutorai.tutoraibe.dto.Request;

import lombok.Data;

@Data
public class CourseRequest {
    private String title;
    private String description;
    private Double price;
    private Integer durationHours;
    private String level;
    private String thumbnailUrl;
    private Long tutorId;
    private Long subjectId;
}
