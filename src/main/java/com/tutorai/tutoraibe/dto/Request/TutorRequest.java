package com.tutorai.tutoraibe.dto.Request;

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
