package com.tutorai.tutoraibe.controller;

import com.tutorai.tutoraibe.dto.Response.StudentResponse;
import com.tutorai.tutoraibe.dto.Request.StudentUpdateRequest;
import com.tutorai.tutoraibe.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/students")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StudentResponse> getMyProfile() {
        return ResponseEntity.ok(studentService.getProfile());
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<StudentResponse> updateMyProfile(@RequestBody StudentUpdateRequest request) {
        return ResponseEntity.ok(studentService.updateProfile(request));
    }
}
