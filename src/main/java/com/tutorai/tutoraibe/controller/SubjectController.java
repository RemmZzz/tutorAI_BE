package com.tutorai.tutoraibe.controller;

import com.tutorai.tutoraibe.dto.Request.SubjectRequest;
import com.tutorai.tutoraibe.dto.Response.SubjectResponse;
import com.tutorai.tutoraibe.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    // Lấy danh sách tất cả môn học
    @GetMapping
    public ResponseEntity<List<SubjectResponse>> getAllSubjects() {
        return ResponseEntity.ok(subjectService.getAll());
    }

    // Lấy chi tiết 1 môn học theo ID
    @GetMapping("/{id}")
    public ResponseEntity<SubjectResponse> getSubjectById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.getById(id));
    }

    // Tạo mới môn học (chỉ ADMIN có quyền)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectResponse> createSubject(@RequestBody SubjectRequest request) {
        return ResponseEntity.ok(subjectService.create(request));
    }

    // Cập nhật thông tin môn học (chỉ ADMIN có quyền)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SubjectResponse> updateSubject(@PathVariable Long id, @RequestBody SubjectRequest request) {
        return ResponseEntity.ok(subjectService.update(id, request));
    }

    // Xóa môn học (chỉ ADMIN có quyền)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteSubject(@PathVariable Long id) {
        subjectService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
