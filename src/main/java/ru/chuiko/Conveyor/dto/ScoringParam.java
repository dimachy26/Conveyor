package ru.chuiko.Conveyor.dto;

import java.util.ArrayList;
import java.util.List;

public class ScoringParam {
    public static List Scor(ScoringDataDTO request){
        List<CreditDTO> numb = new ArrayList<>();
        numb.add(CreateCredit());
        return numb;
    }
    private static CreditDTO CreateCredit(){
        CreditDTO credit = new CreditDTO();
        credit.setTerm(100);
        return credit;
    }
}
