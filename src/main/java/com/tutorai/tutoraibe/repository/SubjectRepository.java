package com.tutorai.tutoraibe.repository;

import com.tutorai.tutoraibe.entity.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByName(String name);
}
