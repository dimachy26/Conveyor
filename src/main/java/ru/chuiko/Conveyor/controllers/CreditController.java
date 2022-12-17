package ru.chuiko.Conveyor.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import ru.chuiko.Conveyor.actions.CreditFacade;
import ru.chuiko.Conveyor.dto.CreditDTO;
import ru.chuiko.Conveyor.dto.LoanApplicationRequestDTO;
import ru.chuiko.Conveyor.dto.LoanOfferDTO;
import ru.chuiko.Conveyor.dto.ScoringDataDTO;

import java.util.List;

@RequiredArgsConstructor
@Component
@RestController
public class CreditController {
    private final CreditFacade facade;

    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDTO>> generateOffers(@RequestBody LoanApplicationRequestDTO request) {
        return ResponseEntity.ok(facade.generateOffers(request));
    }

    @PostMapping("/calculation")
    public ResponseEntity<CreditDTO> createCredit(@RequestBody ScoringDataDTO scoringData){
        return ResponseEntity.ok(facade.createCredit(scoringData));
    }
}
