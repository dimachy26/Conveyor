package ru.chuiko.Conveyor.dto;

import lombok.Data;
import ru.chuiko.Conveyor.enums.Gender;
import ru.chuiko.Conveyor.enums.MaritalStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ScoringDataDTO {
       BigDecimal amount;
       Integer term;
       String firstName;
       String lastName;
       String middleName;
       Gender gender;
       LocalDate birthdate;
       String passportSeries;
       String passportNumber;
       LocalDate passportIssueDate;
       String passportIssueBranch;
       MaritalStatus maritalStatus;
       Integer dependentAmount;
       EmploymentDTO employment;
       String account;
       Boolean isInsuranceEnabled;
       Boolean isSalaryClient;
}
