package ru.chuiko.Conveyor.actions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.chuiko.Conveyor.dto.CreditDTO;
import ru.chuiko.Conveyor.dto.PaymentScheduleElement;
import ru.chuiko.Conveyor.dto.ScoringDataDTO;
import ru.chuiko.Conveyor.enums.EmploymentStatus;
import ru.chuiko.Conveyor.enums.Gender;
import ru.chuiko.Conveyor.enums.MaritalStatus;
import ru.chuiko.Conveyor.enums.Position;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScoringParameter {

    BigDecimal interestRate = BigDecimal.valueOf(25);

    public CreditDTO createCredit(ScoringDataDTO scoringData){
        CreditDTO credit = new CreditDTO();

        scoring(scoringData);

        BigDecimal totalAmount = calculateTotalAmount(scoringData.getAmount(), scoringData.getIsInsuranceEnabled());

        BigDecimal requestedAmount = scoringData.getAmount();

        BigDecimal rate = clientDiscounts(scoringData.getIsInsuranceEnabled(), scoringData.getIsSalaryClient());

        Integer term = scoringData.getTerm();

        BigDecimal monthlyPayment = calculateMonthlyPayment(totalAmount, term, rate);

        List<PaymentScheduleElement> paymentSchedule = calculatePaymentSchedule(totalAmount, scoringData.getTerm(), rate, monthlyPayment);

        BigDecimal psk = calculatePSK(rate, term);

         credit.setAmount(totalAmount);
         credit.setTerm(term);
         credit.setMonthlyPayment(monthlyPayment);
         credit.setRate(rate);
         credit.setPsk(psk);
         credit.setIsInsuranceEnabled(scoringData.getIsInsuranceEnabled());
         credit.setIsSalaryClient(scoringData.getIsSalaryClient());
         credit.setPaymentSchedule(paymentSchedule);

        return credit;
    }

    public BigDecimal calculateTotalAmount(BigDecimal amount, Boolean isInsuranceEnabled){
        if(isInsuranceEnabled){
            BigDecimal insurancePrice = amount.multiply(BigDecimal.valueOf(0.03));
            return amount.add(insurancePrice);
        }
        else{
            return amount;
        }
    }

    public BigDecimal clientDiscounts(Boolean isInsuranceEnabled, Boolean isSalaryClient){
        BigDecimal rate = interestRate;

        if(isInsuranceEnabled){
            rate = rate.subtract(BigDecimal.valueOf(3));
        }

        if(isSalaryClient){
            rate = rate.subtract(BigDecimal.valueOf(1));
        }

        return rate;
    }

    public BigDecimal calculateMonthlyPayment(BigDecimal totalAmount, int term, BigDecimal rate){

        BigDecimal monthlyInterest = rate.divide(BigDecimal.valueOf(12), 5, RoundingMode.CEILING).multiply(BigDecimal.valueOf(0.01));

        BigDecimal monthlyInterestMultiplication = (monthlyInterest.add(BigDecimal.valueOf(1))).pow(term);

        BigDecimal monthlyInterestDivision = monthlyInterestMultiplication.subtract(BigDecimal.valueOf(1));

        BigDecimal annuityRatio = monthlyInterest.multiply(monthlyInterestMultiplication).divide(monthlyInterestDivision, 5, RoundingMode.CEILING);

        BigDecimal monthlyPayment = totalAmount.multiply(annuityRatio).setScale(2, RoundingMode.CEILING);

        return monthlyPayment;
    }

    public BigDecimal calculatePSK(BigDecimal rate, int term){

        BigDecimal psk = rate.multiply(BigDecimal.valueOf(term)).multiply(BigDecimal.valueOf(100));

        return psk;
    }

    public List<PaymentScheduleElement> calculatePaymentSchedule(BigDecimal totalAmount, Integer term, BigDecimal rate, BigDecimal monthlyPayment){

        List<PaymentScheduleElement> paymentSchedule = new ArrayList<>();

        BigDecimal remainingDebt = totalAmount;

        LocalDate paymentDate = LocalDate.now();

        for (int i = 1; i < term + 1; i++) {
            PaymentScheduleElement paymentScheduleElement = new PaymentScheduleElement();
            paymentDate = paymentDate.plusMonths(1);

            BigDecimal interestPayment = remainingDebt.multiply(rate).divide(rate.add(BigDecimal.valueOf(1)), 2, RoundingMode.CEILING).subtract(BigDecimal.valueOf(term)).subtract(BigDecimal.valueOf(1));
            //remainingDebt*(rate/(1+rate)-term-1)

            BigDecimal debtPayment = monthlyPayment.subtract(interestPayment);

            remainingDebt = remainingDebt.subtract(debtPayment);

            paymentScheduleElement.setNumber(i);
            paymentScheduleElement.setDate(paymentDate);
            paymentScheduleElement.setTotalPayment(monthlyPayment);
            paymentScheduleElement.setInterestPayment(interestPayment);
            paymentScheduleElement.setDebtPayment(debtPayment);
            paymentScheduleElement.setRemainingDebt(remainingDebt);

            paymentSchedule.add(paymentScheduleElement);
        }

        return paymentSchedule;
    }

    public void scoring(ScoringDataDTO scoringData){

        BigDecimal currentRate = interestRate;

        int clientAge = Period.between(scoringData.getBirthdate(), LocalDate.now()).getYears();

        if(scoringData.getEmployment().getEmploymentStatus() == EmploymentStatus.UNEMPLOYED){
            log.info("Refusal! Reason: Client is unemployed");
            throw new RuntimeException("Refusal! Reason: Client is unemployed");
        }
        else if(scoringData.getEmployment().getEmploymentStatus() == EmploymentStatus.EMPLOYED){
            log.info("The interest rate on the loan increased by 1 units");
            currentRate = currentRate.add(BigDecimal.valueOf(1));
        }
        else if(scoringData.getEmployment().getEmploymentStatus() == EmploymentStatus.OWNER){
            log.info("The interest rate on the loan increased by 3 units");
            currentRate = currentRate.add(BigDecimal.valueOf(3));
        }

        if(scoringData.getEmployment().getPosition() == Position.MIDDLE){
            log.info("The interest rate on the loan decreased by 2 units");
            currentRate = currentRate.subtract(BigDecimal.valueOf(2));
        }
        else if(scoringData.getEmployment().getPosition() == Position.TOP){
            log.info("The interest rate on the loan decreased by 4 units");
            currentRate = currentRate.subtract(BigDecimal.valueOf(4));
        }

        if(scoringData.getAmount().hashCode() > scoringData.getEmployment().getSalary().multiply(BigDecimal.valueOf(20)).hashCode()){
            log.info("Refusal! Reason: large loan amount (in relation to salary)");
            throw new RuntimeException("Refusal! Reason: large loan amount (in relation to salary)");
        }

        if(scoringData.getMaritalStatus() == MaritalStatus.MARRIED){
            log.info("The interest rate on the loan decreased by 3 units");
            currentRate = currentRate.subtract(BigDecimal.valueOf(3));
        }
        else if(scoringData.getMaritalStatus() == MaritalStatus.DIVORCED){
            log.info("The interest rate on the loan increased by 1 units");
            currentRate = currentRate.add(BigDecimal.valueOf(1));
        }

        if(scoringData.getDependentAmount() > 1){
            log.info("The interest rate on the loan increased by 1 units");
            currentRate = currentRate.add(BigDecimal.valueOf(1));
        }

        if(clientAge < 20){
            log.info("Refusal! Reason: Client is too young");
            throw new RuntimeException("Refusal! Reason: Client is too young");
        }
        else if(clientAge > 60){
            log.info("Refusal! Reason: Client is too old");
            throw new RuntimeException("Refusal! Reason: Client is too old");
        }

        if(scoringData.getGender() == Gender.FEMALE && (clientAge > 30 && clientAge < 60)){
            log.info("The interest rate on the loan decreased by 3 units");
            currentRate = currentRate.subtract(BigDecimal.valueOf(3));
        }
        else if(scoringData.getGender() == Gender.MALE && (clientAge > 30 && clientAge < 55)){
            log.info("The interest rate on the loan decreased by 3 units");
            currentRate = currentRate.subtract(BigDecimal.valueOf(3));
        }
        else if(scoringData.getGender() == Gender.ANOTHER){
            log.info("Refusal! Reason: The client is strange, we do not work with such");
            throw new RuntimeException("Refusal! Reason: The client is strange, we do not work with such");
        }

        if(scoringData.getEmployment().getWorkExperienceTotal() < 12 ){
            log.info("Refusal! Reason: Total work experience less than 12 months");
            throw new RuntimeException("Refusal! Reason: Current work experience less than 12 months");
        }

        if(scoringData.getEmployment().getWorkExperienceCurrent() < 3 ){
            log.info("Refusal! Reason: Current work experience less than 3 months");
            throw new RuntimeException("Refusal! Reason: Current work experience less than 3 months");
        }

        interestRate = currentRate;
    }
}
