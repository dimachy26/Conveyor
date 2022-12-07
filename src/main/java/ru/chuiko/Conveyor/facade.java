package ru.chuiko.Conveyor;

import org.springframework.web.bind.annotation.ResponseBody;
import ru.chuiko.Conveyor.dto.*;

import java.util.ArrayList;
import java.util.List;

public class facade{
    public static List generateOffers(LoanApplicationRequestDTO request){
        List<LoanOfferDTO> LoanOffers = GenerateAnswer.GenerateOff(request);
        return LoanOffers;
    }
    public static List scoring(ScoringDataDTO request){
        List<CreditDTO> Credit = ScoringParam.Scor(request);
        return Credit;
    }
}

