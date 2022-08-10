package com.example.students_crud.repos;

import com.example.students_crud.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;


public interface StudentRepository extends JpaRepository<Student, Integer> {

    boolean existsByMatriculaIgnoreCase(String matricula);

    boolean existsByEmailIgnoreCase(String email);

}
