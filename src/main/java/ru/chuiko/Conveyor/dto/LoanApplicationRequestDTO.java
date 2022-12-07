package ru.chuiko.Conveyor.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Data
public class LoanApplicationRequestDTO {
    private String firstName;
    private String lastName;
    private BigDecimal amount;
    private Integer term;
    private String middleName;
    private String email;
    private String passportSeries;
    private LocalDate birthdate;

    public void setBirthdate(String val) {
        this.birthdate = LocalDate.parse(val, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }
}
