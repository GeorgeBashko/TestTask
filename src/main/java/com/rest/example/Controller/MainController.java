package com.rest.example.Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rest.example.DTO.PatientDto;
import com.rest.example.Entities.Patient;
import com.rest.example.repository.PatientsRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Tag(name = "main_methods")
@Slf4j
@RestController
@RequiredArgsConstructor
public class MainController {
    private final PatientsRepository patientsRepository;
    private final ObjectMapper objectMapper;
    private List<PatientDto> generatePatients(int count) {
        List<PatientDto> patients = new ArrayList<>();
        Random random = new Random();

        String[] names = {"Ivan", "Bob", "Charlie", "David", "Emma", "Frank", "Grace", "Henry", "Ivy", "Jack"};
        String[] genders = {"Male", "Female"};

        for (int i = 0; i < count; i++) {
            String name = names[random.nextInt(names.length)];
            String gender = genders[random.nextInt(genders.length)];
            String birthDate = generateRandomDate(1960, 2020);

            patients.add(new PatientDto(name, gender, birthDate));
        }

        return patients;
    }

    private String generateRandomDate(int startYear, int endYear) {
        Random random = new Random();
        int year = random.nextInt(endYear - startYear + 1) + startYear;
        int month = random.nextInt(12) + 1;
        int day = random.nextInt(28) + 1;
        return LocalDate.of(year, month, day).toString();
    }
    @Operation(
            summary = "Add 100 patients in database",
            description = "This method doesn't receive any parameters"
    )
    @PostMapping("/init")
    public String init() {
        List<PatientDto> patients = generatePatients(100);
        for (int i = 0;i < 100;i++){
            log.info("New patient : " + patientsRepository.save(Patient.builder().name(patients.get(i).getName()).Gender(patients.get(i).getGender()).birthDate(patients.get(i).getBirthDate()).build()));
        }
        System.out.println("Database was initialized");
        return "Database was initialized (100 users was added)";
    }
    @Operation(
            summary = "Add a certain patient",
            description = "This method receives PatientDTO object"
    )
    @PostMapping("/api/v1/add")
    public void addPatient(@RequestBody PatientDto patientDto) {
        log.info("New patient : " + patientsRepository.save(Patient.builder()
                        .name(patientDto.getName())
                        .Gender(patientDto.getGender())
                        .birthDate(patientDto.getBirthDate())
                        .build())
        );
    }
    @Operation(
            summary = "Get all patients",
            description = "This method doesn't receive any parameters"
    )
    @SneakyThrows
    @GetMapping("/api/v1/patients")
    public String getAllPatients() {
        List<Patient> patients = patientsRepository.findAll();
        return objectMapper.writeValueAsString(patients);
    }
    @Operation(
            summary = "Get patient by Id",
            description = "This method receives Id in the path"
    )
    @GetMapping("/api/v1/patient/{id}")
    public Patient getPatientById(@PathVariable("id") String id) {
        return patientsRepository.findById(id).orElseThrow();
    }
    @Operation(
            summary = "Delete patient by Id",
            description = "This method receives Id in the path"
    )
    @DeleteMapping("/api/v1/delete/{id}")
    public void deletePatientById(@PathVariable("id") String id) {
        patientsRepository.deleteById(id);
    }
    @Operation(
            summary = "Update a certain patient",
            description = "This method receives Patient object"
    )
    @PutMapping("/api/v1/update")
    public String updatePatientById(@RequestBody Patient patient) {
        if (!patientsRepository.existsById("" + patient.getId())) {
            return "Patient not found";
        }
        log.info("Change patient: " + patientsRepository.save(patient));
        return patient.toString();
    }
}
