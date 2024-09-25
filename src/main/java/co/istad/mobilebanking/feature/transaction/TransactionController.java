package co.istad.mobilebanking.feature.transaction;

import co.istad.mobilebanking.feature.transaction.dto.TransactionRequest;
import co.istad.mobilebanking.feature.transaction.dto.TransactionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping("/transfers")
    TransactionResponse transactionResponse(@Valid @RequestBody TransactionRequest transactionRequest) {
     return transactionService.transfer(transactionRequest);
    }



}
