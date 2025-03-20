package com.rest.example.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@Table(name = "patients")
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String Gender; //enum
    private String birthDate;

    public Patient(String name, String Gender, String birthDate) {
        this.name = name;
        this.Gender = Gender;
        this.birthDate = birthDate;
    }

    public Patient() {
    }

    @Override
    public String toString() {
        return "Patient{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", Gender='" + Gender + '\'' +
                ", birthDate='" + birthDate + '\'' +
                '}';
    }
}
