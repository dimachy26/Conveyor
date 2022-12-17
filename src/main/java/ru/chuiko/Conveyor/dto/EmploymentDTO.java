package ru.chuiko.Conveyor.dto;

import lombok.Data;
import ru.chuiko.Conveyor.enums.EmploymentStatus;
import ru.chuiko.Conveyor.enums.Position;

import java.math.BigDecimal;

@Data
public class EmploymentDTO {
       private EmploymentStatus employmentStatus;
       private String employerINN;
       private BigDecimal salary;
       private Position position;
       private Integer workExperienceTotal;
       private Integer workExperienceCurrent;
}
