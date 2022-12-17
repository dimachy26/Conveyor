package ru.chuiko.Conveyor.dto;

import lombok.Data;
import ru.chuiko.Conveyor.enums.EmploymentStatus;
import ru.chuiko.Conveyor.enums.Position;

import java.math.BigDecimal;

@Data
public class EmploymentDTO {
       EmploymentStatus employmentStatus;
       String employerINN;
       BigDecimal salary;
       Position position;
       Integer workExperienceTotal;
       Integer workExperienceCurrent;
}
