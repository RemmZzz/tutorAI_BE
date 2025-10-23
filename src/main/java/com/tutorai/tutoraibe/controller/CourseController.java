package com.tutorai.tutoraibe.controller;

import com.tutorai.tutoraibe.dto.Request.CourseRequest;
import com.tutorai.tutoraibe.dto.Response.CourseResponse;
import com.tutorai.tutoraibe.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    // Lấy danh sách tất cả khóa học
    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    // Lấy chi tiết 1 khóa học theo ID
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    // Tạo mới khóa học (Admin hoặc Tutor)
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','TUTOR')")
    public ResponseEntity<CourseResponse> createCourse(@RequestBody CourseRequest request) {
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    // Cập nhật thông tin khóa học
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','TUTOR')")
    public ResponseEntity<CourseResponse> updateCourse(
            @PathVariable Long id,
            @RequestBody CourseRequest request
    ) {
        return ResponseEntity.ok(courseService.updateCourse(id, request));
    }

    // Xóa khóa học
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}
