package com.tutorai.tutoraibe.dto.Response;

import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private Double price;
    private String level;
    private String thumbnailUrl;
    private String tutorName;
    private String subjectName;
    private LocalDateTime createdAt;
}
