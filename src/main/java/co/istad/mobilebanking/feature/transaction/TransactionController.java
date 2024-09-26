package co.istad.mobilebanking.feature.transaction;

import co.istad.mobilebanking.feature.transaction.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/transfers")
    TransactionResponse transactionResponse(@Valid @RequestBody TransactionRequest transactionRequest) {
     return transactionService.transfer(transactionRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/top-up")
    TopUpReponse mobileTopUp(@Valid @RequestBody TopUpRequest topUpRequest){
        return transactionService.mobileTopUp(topUpRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/education-payment")
    TransactionResponse educationPayment(@Valid @RequestBody EducationPaymentRequest educationPaymentRequest){
        return transactionService.educationPayment(educationPaymentRequest);
    }



}
