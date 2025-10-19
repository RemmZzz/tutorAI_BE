package com.tutorai.tutoraibe.dto;

import lombok.*;

@Getter
@Setter
public class TutorRequest {
    private String fullName;
    private String subject;
    private Integer experienceYears;
    private Double hourlyRate;
    private String bio;
}
