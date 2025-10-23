package com.tutorai.tutoraibe.dto.Response;

import com.tutorai.tutoraibe.entity.Subject.Status;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubjectResponse {
    private Long id;
    private String name;
    private String category;
    private String description;
    private String iconUrl;
    private Status status;
    private String createdAt;
}