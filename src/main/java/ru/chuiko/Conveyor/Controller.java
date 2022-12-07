package ru.chuiko.Conveyor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.chuiko.Conveyor.dto.CreditDTO;
import ru.chuiko.Conveyor.dto.LoanApplicationRequestDTO;
import ru.chuiko.Conveyor.dto.LoanOfferDTO;
import ru.chuiko.Conveyor.dto.ScoringDataDTO;

import java.util.List;

@RestController
public class Controller {

    @ResponseBody
    @PostMapping("/offers")
    public ResponseEntity<List<LoanOfferDTO>> generateOffers(@RequestBody LoanApplicationRequestDTO request) {
        return ResponseEntity.ok(facade.generateOffers(request));
    }

    @PostMapping("/calculation")
    public ResponseEntity<List<CreditDTO>> scoring(@RequestBody ScoringDataDTO request){
        return ResponseEntity.ok(facade.scoring(request));
    }
}
