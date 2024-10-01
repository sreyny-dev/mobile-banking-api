package co.istad.mobilebanking.feature.transaction;

import co.istad.mobilebanking.feature.transaction.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/transfers")
    TransactionResponse transferMoney(@Valid @RequestBody TransactionRequest transactionRequest) {
     return transactionService.transfer(transactionRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/top-up")
    TopUpReponse mobileTopUp(@Valid @RequestBody TopUpRequest topUpRequest){
        return transactionService.mobileTopUp(topUpRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/education-payment")
    TransactionResponse educationPayment(@Valid @RequestBody EducationPaymentRequest educationPaymentRequest){
        return transactionService.educationPayment(educationPaymentRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/account/{actNo}")
    List<TransactionResponse> accountAllTransaction(@Valid @PathVariable String actNo){
        return transactionService.getTransactions(actNo);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping()
    List<TransactionResponse> findAllTransaction(){
        return transactionService.getAllTransactions();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/between-accounts/{senderActNo}/{receiverActNo}")
    List<TransactionResponse> findAllBetweenAccount(@PathVariable String senderActNo, @PathVariable String receiverActNo){
        return transactionService.getTransactionsBetweenAccounts(senderActNo, receiverActNo);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/get-income/{actNo}")
    List<TransactionResponse> findIncome(@PathVariable String actNo){
        return transactionService.getIncome(actNo);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/get-expense/{actNo}")
    List<TransactionResponse> findExpense(@PathVariable String actNo){
        return transactionService.getExpense(actNo);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/id/{id}")
    void deleteTransactionById(@PathVariable Integer id){
         transactionService.deleteTransactionById(id);
    }


}
