package com.tutorai.tutoraibe.service;

import com.tutorai.tutoraibe.dto.Request.SubjectRequest;
import com.tutorai.tutoraibe.dto.Response.SubjectResponse;
import com.tutorai.tutoraibe.entity.Subject;
import com.tutorai.tutoraibe.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public List<SubjectResponse> getAll() {
        return subjectRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public SubjectResponse getById(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Subject not found"));
        return mapToResponse(subject);
    }

    public SubjectResponse create(SubjectRequest request) {
        if (subjectRepository.existsByName(request.getName())) {
            throw new IllegalArgumentException("Subject name already exists");
        }

        Subject subject = Subject.builder()
                .name(request.getName())
                .category(request.getCategory())
                .description(request.getDescription())
                .iconUrl(request.getIconUrl())
                .status(request.getStatus() != null ? request.getStatus() : Subject.Status.ACTIVE)
                .build();

        subjectRepository.save(subject);
        return mapToResponse(subject);
    }

    public SubjectResponse update(Long id, SubjectRequest request) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Subject not found"));

        subject.setName(request.getName());
        subject.setCategory(request.getCategory());
        subject.setDescription(request.getDescription());
        subject.setIconUrl(request.getIconUrl());
        subject.setStatus(request.getStatus());
        subjectRepository.save(subject);

        return mapToResponse(subject);
    }

    public void delete(Long id) {
        Subject subject = subjectRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Subject not found"));
        subjectRepository.delete(subject);
    }

    private SubjectResponse mapToResponse(Subject subject) {
        return SubjectResponse.builder()
                .id(subject.getId())
                .name(subject.getName())
                .category(subject.getCategory())
                .description(subject.getDescription())
                .iconUrl(subject.getIconUrl())
                .status(subject.getStatus())
                .createdAt(subject.getCreatedAt() != null ? subject.getCreatedAt().format(formatter) : null)
                .build();
    }
}