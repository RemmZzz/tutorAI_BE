package com.tutorai.tutoraibe.dto.Request;

import com.tutorai.tutoraibe.entity.enums.SchoolLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentUpdateRequest {
    private SchoolLevel schoolLevel;
    private String goal;
}
