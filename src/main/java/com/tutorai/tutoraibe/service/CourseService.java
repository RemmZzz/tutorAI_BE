package com.tutorai.tutoraibe.service;

import com.tutorai.tutoraibe.dto.Request.CourseRequest;
import com.tutorai.tutoraibe.dto.Response.CourseResponse;
import com.tutorai.tutoraibe.entity.Course;
import com.tutorai.tutoraibe.entity.Subject;
import com.tutorai.tutoraibe.entity.Tutor;
import com.tutorai.tutoraibe.exception.ResourceNotFoundException;
import com.tutorai.tutoraibe.repository.CourseRepository;
import com.tutorai.tutoraibe.repository.SubjectRepository;
import com.tutorai.tutoraibe.repository.TutorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final TutorRepository tutorRepository;
    private final SubjectRepository subjectRepository;

    // Lấy tất cả khóa học
    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // Lấy khóa học theo ID
    public CourseResponse getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
        return mapToResponse(course);
    }

    // Tạo khóa học mới
    public CourseResponse createCourse(CourseRequest request) {
        Tutor tutor = tutorRepository.findById(request.getTutorId())
                .orElseThrow(() -> new ResourceNotFoundException("Tutor not found"));
        Subject subject = subjectRepository.findById(request.getSubjectId())
                .orElseThrow(() -> new ResourceNotFoundException("Subject not found"));

        Course course = new Course();
        BeanUtils.copyProperties(request, course);
        course.setTutor(tutor);
        course.setSubject(subject);
        courseRepository.save(course);

        return mapToResponse(course);
    }

    // Cập nhật khóa học
    public CourseResponse updateCourse(Long id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        BeanUtils.copyProperties(request, course, "id");
        courseRepository.save(course);

        return mapToResponse(course);
    }

    // Xóa khóa học
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new ResourceNotFoundException("Course not found");
        }
        courseRepository.deleteById(id);
    }

    // Mapping từ entity → response DTO
    private CourseResponse mapToResponse(Course course) {
        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .price(course.getPrice())
                .level(course.getLevel())
                .thumbnailUrl(course.getThumbnailUrl())
                .tutorName(course.getTutor().getFullName())
                .subjectName(course.getSubject().getName())
                .createdAt(course.getCreatedAt())
                .build();
    }
}
