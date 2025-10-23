package com.tutorai.tutoraibe.service;

import com.tutorai.tutoraibe.dto.Request.TutorRequest;
import com.tutorai.tutoraibe.entity.*;
import com.tutorai.tutoraibe.repository.TutorRepository;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TutorService {
    private final TutorRepository tutorRepository;

    public List<Tutor> search(String keyword, String subject, Double minRate, Double maxRate) {
        return tutorRepository.findAll((root, query, cb) -> {
            Predicate p = cb.conjunction();
            if (keyword != null && !keyword.isEmpty())
                p = cb.and(p, cb.like(cb.lower(root.get("fullName")), "%" + keyword.toLowerCase() + "%"));
            if (subject != null && !subject.isEmpty())
                p = cb.and(p, cb.equal(root.get("subject"), subject));
            if (minRate != null)
                p = cb.and(p, cb.ge(root.get("hourlyRate"), minRate));
            if (maxRate != null)
                p = cb.and(p, cb.le(root.get("hourlyRate"), maxRate));
            return p;
        });
    }

    public Tutor createTutor(User user, TutorRequest dto) {
        Tutor tutor = Tutor.builder()
                .user(user)
                .fullName(dto.getFullName())
                .subject(dto.getSubject())
                .experienceYears(dto.getExperienceYears())
                .hourlyRate(dto.getHourlyRate())
                .bio(dto.getBio())
                .status(Tutor.Status.PENDING)
                .build();
        return tutorRepository.save(tutor);
    }

    public Tutor updateTutor(Long id, TutorRequest dto) {
        Tutor t = tutorRepository.findById(id).orElseThrow();
        t.setFullName(dto.getFullName());
        t.setSubject(dto.getSubject());
        t.setExperienceYears(dto.getExperienceYears());
        t.setHourlyRate(dto.getHourlyRate());
        t.setBio(dto.getBio());
        return tutorRepository.save(t);
    }

    public Tutor approve(Long id) {
        Tutor t = tutorRepository.findById(id).orElseThrow();
        t.setStatus(Tutor.Status.APPROVED);
        return tutorRepository.save(t);
    }

    public Tutor hide(Long id) {
        Tutor t = tutorRepository.findById(id).orElseThrow();
        t.setStatus(Tutor.Status.HIDDEN);
        return tutorRepository.save(t);
    }

    public void delete(Long id) {
        tutorRepository.deleteById(id);
    }

    public Tutor getById(Long id) {
        return tutorRepository.findById(id).orElseThrow();
    }
}
