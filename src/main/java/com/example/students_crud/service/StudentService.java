package com.example.students_crud.service;

import com.example.students_crud.domain.Student;
import com.example.students_crud.model.StudentDTO;
import com.example.students_crud.repos.StudentRepository;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;


@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(final StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<StudentDTO> findAll() {
        return studentRepository.findAll(Sort.by("id"))
                .stream()
                .map(student -> mapToDTO(student, new StudentDTO()))
                .collect(Collectors.toList());
    }

    public StudentDTO get(final Integer id) {
        return studentRepository.findById(id)
                .map(student -> mapToDTO(student, new StudentDTO()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Integer create(final StudentDTO studentDTO) {
        final Student student = new Student();
        mapToEntity(studentDTO, student);
        return studentRepository.save(student).getId();
    }

    public void update(final Integer id, final StudentDTO studentDTO) {
        final Student student = studentRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        mapToEntity(studentDTO, student);
        studentRepository.save(student);
    }

    public void delete(final Integer id) {
        studentRepository.deleteById(id);
    }

    private StudentDTO mapToDTO(final Student student, final StudentDTO studentDTO) {
        studentDTO.setId(student.getId());
        studentDTO.setName(student.getName());
        studentDTO.setLastname(student.getLastname());
        studentDTO.setAge(student.getAge());
        studentDTO.setMajor(student.getMajor());
        studentDTO.setMatricula(student.getMatricula());
        studentDTO.setQuater(student.getQuater());
        studentDTO.setEmail(student.getEmail());
        return studentDTO;
    }

    private Student mapToEntity(final StudentDTO studentDTO, final Student student) {
        student.setName(studentDTO.getName());
        student.setLastname(studentDTO.getLastname());
        student.setAge(studentDTO.getAge());
        student.setMajor(studentDTO.getMajor());
        student.setMatricula(studentDTO.getMatricula());
        student.setQuater(studentDTO.getQuater());
        student.setEmail(studentDTO.getEmail());
        return student;
    }

    public boolean matriculaExists(final String matricula) {
        return studentRepository.existsByMatriculaIgnoreCase(matricula);
    }

    public boolean emailExists(final String email) {
        return studentRepository.existsByEmailIgnoreCase(email);
    }

}
