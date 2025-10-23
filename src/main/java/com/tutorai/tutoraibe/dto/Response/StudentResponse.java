package com.tutorai.tutoraibe.dto.Response;

import com.tutorai.tutoraibe.entity.enums.SchoolLevel;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentResponse {
    private Long id;
    private String fullName;
    private String email;
    private SchoolLevel schoolLevel;
    private String goal;
}
