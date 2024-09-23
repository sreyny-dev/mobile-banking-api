package co.istad.mobilebanking.feature.account;

import co.istad.mobilebanking.feature.account.dto.AccountResponse;
import co.istad.mobilebanking.feature.account.dto.CreateAccountRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest){
        return accountService.createAccount(createAccountRequest);
    }

    @GetMapping
    List<AccountResponse> getAllAccounts(){
        return accountService.findAllAccount();
    }

    @GetMapping("/actNo/{actNo}")
    AccountResponse findAccountByActNo(@PathVariable String actNo){
        return accountService.findAccountByActNo(actNo);
    }

    @GetMapping("phoneNumber/{phoneNumber}")
    List<AccountResponse> findAccountByPhoneNumber(@PathVariable String phoneNumber){
        return accountService.findAccountByPhoneNumber(phoneNumber);
    }

}
