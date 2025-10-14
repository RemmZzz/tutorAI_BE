package com.tutorai.tutoraibe.repository;

import com.tutorai.tutoraibe.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, Long> {}
