package ru.chuiko.Conveyor;

import ru.chuiko.Conveyor.dto.LoanApplicationRequestDTO;
import ru.chuiko.Conveyor.dto.LoanOfferDTO;

import java.util.ArrayList;
import java.util.List;

public class GenerateAnswer {
    static private Integer main_stage = 7;
    private static LoanApplicationRequestDTO request_;

    public static List GenerateOff(LoanApplicationRequestDTO request){
        request_ = request;
        List<LoanOfferDTO> numbers = new ArrayList<>();
        numbers.add(CreateLoanOffer(true, true));
        numbers.add(CreateLoanOffer(false, false));
        numbers.add(CreateLoanOffer(true, false));
        numbers.add(CreateLoanOffer(false, true));
        return numbers;
    }

    private static LoanOfferDTO CreateLoanOffer(boolean is_insurance_enabled, boolean is_salary_client){
        LoanOfferDTO loanOffer = new LoanOfferDTO();
        return loanOffer;
    }
}
