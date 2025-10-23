package com.tutorai.tutoraibe.repository;

import com.tutorai.tutoraibe.entity.Student;
import com.tutorai.tutoraibe.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUser(User user);
}
