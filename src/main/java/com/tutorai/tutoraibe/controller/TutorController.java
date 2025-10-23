package com.tutorai.tutoraibe.controller;

import com.tutorai.tutoraibe.dto.Request.TutorRequest;
import com.tutorai.tutoraibe.entity.Tutor;
import com.tutorai.tutoraibe.entity.User;
import com.tutorai.tutoraibe.service.TutorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tutors")
@RequiredArgsConstructor
public class TutorController {
    private final TutorService tutorService;

    @GetMapping("/search")
    public ResponseEntity<List<Tutor>> search(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String subject,
            @RequestParam(required = false) Double minRate,
            @RequestParam(required = false) Double maxRate) {
        return ResponseEntity.ok(tutorService.search(keyword, subject, minRate, maxRate));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tutor> getTutor(@PathVariable Long id) {
        return ResponseEntity.ok(tutorService.getById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('TUTOR')")
    public ResponseEntity<Tutor> create(@RequestBody TutorRequest request, @RequestAttribute("user") User user) {
        return ResponseEntity.ok(tutorService.createTutor(user, request));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TUTOR','ADMIN')")
    public ResponseEntity<Tutor> update(@PathVariable Long id, @RequestBody TutorRequest request) {
        return ResponseEntity.ok(tutorService.updateTutor(id, request));
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tutor> approve(@PathVariable Long id) {
        return ResponseEntity.ok(tutorService.approve(id));
    }

    @PatchMapping("/{id}/hide")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Tutor> hide(@PathVariable Long id) {
        return ResponseEntity.ok(tutorService.hide(id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        tutorService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
