package com.tutorai.tutoraibe.dto;

import lombok.*;

@Getter
@Setter
@Builder
public class TutorResponse {
    private Long id;
    private String fullName;
    private String subject;
    private Integer experienceYears;
    private Double hourlyRate;
    private String bio;
    private String status;
}
