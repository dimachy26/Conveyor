package ru.chuiko.Conveyor.actions;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.chuiko.Conveyor.dto.LoanApplicationRequestDTO;
import ru.chuiko.Conveyor.dto.LoanOfferDTO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Component
@Slf4j
public class GenerateAnswer {
    private final ScoringParameter scoringParameter;

    public List loanOfferList(LoanApplicationRequestDTO request){

        List<LoanOfferDTO> offerNum = new ArrayList<>();
        offerNum.add(createOffer(true, true, request));
        offerNum.add(createOffer(false, false, request));
        offerNum.add(createOffer(true, false, request));
        offerNum.add(createOffer(false, true, request));

        return offerNum;
    }

    private  LoanOfferDTO createOffer(boolean isInsuranceEnabled, boolean isSalaryClient, LoanApplicationRequestDTO request){
        LoanOfferDTO loan = new LoanOfferDTO();

        BigDecimal totalAmount = scoringParameter.calculateTotalAmount(request.getAmount(), isInsuranceEnabled);
        BigDecimal rate = scoringParameter.clientDiscounts(isInsuranceEnabled, isSalaryClient);
        long id = 100L;

        Random random = new Random();

        prescoring(request);

        loan.setApplicationId(random.nextLong(id));
        loan.setRequestedAmount(request.getAmount());
        loan.setTotalAmount(totalAmount);
        loan.setTerm(request.getTerm());
        loan.setMonthlyPayment(scoringParameter.calculateMonthlyPayment(totalAmount, request.getTerm(), rate));
        loan.setRate(rate);
        loan.setIsInsuranceEnabled(isInsuranceEnabled);
        loan.setIsSalaryClient(isSalaryClient);

        return loan;
    }

    public void prescoring(LoanApplicationRequestDTO request){

        int clientAge = Period.between(request.getBirthdate(), LocalDate.now()).getYears();

        BigDecimal amount = BigDecimal.valueOf(10000);

        Pattern emailPattern = Pattern.compile("[\\w\\.]{2,50}@[\\w\\.]{2,20}");
        Matcher emailMatcher = emailPattern.matcher(request.getEmail());

        Pattern passportSeriesPattern = Pattern.compile("[0-9]{2}\s[0-9]{2}");
        Matcher passportSeriesMatcher = passportSeriesPattern.matcher(request.getPassportSeries());

        Pattern passportNumberPattern = Pattern.compile("[0-9]{6}");
        Matcher passportNumberMatcher = passportNumberPattern.matcher(request.getPassportNumber());

        if(request.getFirstName().length() < 2){
            log.info("Wrong data: First name is too short");
            throw new RuntimeException("Wrong data: First name is too short");
        }
        if(request.getFirstName().length() > 30){
            log.info("Wrong data: First name is too long");
            throw new RuntimeException("Wrong data: First name is too long");
        }

        if(request.getLastName().length() < 2){
            log.info("Wrong data: Last name is too short");
            throw new RuntimeException("Wrong data: Last name is too short");
        }
        if(request.getLastName().length() > 30){
            log.info("Wrong data: Last name is too long");
            throw new RuntimeException("Wrong data: Last name is too long");
        }

        if(request.getMiddleName().length() < 2){
            log.info("Wrong data: Middle name is too short");
            throw new RuntimeException("Wrong data: Middle name is too short");
        }
        if(request.getMiddleName().length() > 30){
            log.info("Wrong data: Middle name is too long");
            throw new RuntimeException("Wrong data: Middle name is too long");
        }

        if(request.getAmount().compareTo(amount) < 0){
            log.info("Wrong data: Small loan amount");
            throw new RuntimeException("Wrong data: Small loan amount");
        }

        if(request.getTerm() < 6){
            log.info("Wrong data: Short term loan");
            throw new RuntimeException("Wrong data: Short term loan");
        }

        if(clientAge < 18){
            log.info("Wrong data: The client is too young");
            throw new RuntimeException("Wrong data: The client is too young");
        }

        if(!emailMatcher.matches()){
            log.info("Wrong data: Bad Email format");
            throw new RuntimeException("Wrong data: Bad Email format");
        }

        if(!passportSeriesMatcher.matches()){
            log.info("Wrong data: Bad passport series format");
            throw new RuntimeException("Wrong data: Bad passport series format");
        }
        if(!passportNumberMatcher.matches()){
            log.info("Wrong data: Bad passport number format");
            throw new RuntimeException("Wrong data: Bad passport number format");
        }
    }

}
