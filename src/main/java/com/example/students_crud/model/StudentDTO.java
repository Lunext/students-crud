package com.example.students_crud.model;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


public class StudentDTO {

    private Integer id;

    @NotNull
    @Size(max = 255)
    private String name;

    @NotNull
    @Size(max = 255)
    private String lastname;

    @NotNull
    private Integer age;

    @NotNull
    @Size(max = 255)
    private String major;

    @NotNull
    @Size(max = 255)
    private String matricula;

    @NotNull
    @Size(max = 255)
    private String quater;

    @NotNull
    @Size(max = 255)
    private String email;

    public Integer getId() {
        return id;
    }

    public void setId(final Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(final String lastname) {
        this.lastname = lastname;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(final Integer age) {
        this.age = age;
    }

    public String getMajor() {
        return major;
    }

    public void setMajor(final String major) {
        this.major = major;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(final String matricula) {
        this.matricula = matricula;
    }

    public String getQuater() {
        return quater;
    }

    public void setQuater(final String quater) {
        this.quater = quater;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

}
