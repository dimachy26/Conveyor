package ru.chuiko.Conveyor.actions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.chuiko.Conveyor.dto.*;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CreditFacade {
    private final GenerateAnswer generateAnswer;
    private final ScoringParameter scoringParameter;
    public List generateOffers(LoanApplicationRequestDTO request){
        List<LoanOfferDTO> loanOffers = generateAnswer.loanOfferList(request);
        return loanOffers;
    }

    public CreditDTO createCredit(ScoringDataDTO scoringData){
        CreditDTO credit = scoringParameter.createCredit(scoringData);
        return credit;
    }
}

