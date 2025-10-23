package com.tutorai.tutoraibe.service;

import com.tutorai.tutoraibe.dto.Response.StudentResponse;
import com.tutorai.tutoraibe.dto.Request.StudentUpdateRequest;
import com.tutorai.tutoraibe.entity.Student;
import com.tutorai.tutoraibe.entity.User;
import com.tutorai.tutoraibe.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;

    public StudentResponse getProfile() {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Student student = studentRepository.findByUser(currentUser)
                .orElseThrow(() -> new NoSuchElementException("Student profile not found"));

        return StudentResponse.builder()
                .id(student.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getEmail())
                .schoolLevel(student.getSchoolLevel())
                .goal(student.getGoal())
                .build();
    }

    public StudentResponse updateProfile(StudentUpdateRequest request) {
        User currentUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Student student = studentRepository.findByUser(currentUser)
                .orElseThrow(() -> new NoSuchElementException("Student profile not found"));

        student.setSchoolLevel(request.getSchoolLevel());
        student.setGoal(request.getGoal());
        studentRepository.save(student);

        return StudentResponse.builder()
                .id(student.getId())
                .fullName(currentUser.getFullName())
                .email(currentUser.getEmail())
                .schoolLevel(student.getSchoolLevel())
                .goal(student.getGoal())
                .build();
    }
}
