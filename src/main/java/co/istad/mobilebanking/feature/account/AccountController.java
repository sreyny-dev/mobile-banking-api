package co.istad.mobilebanking.feature.account;

import co.istad.mobilebanking.feature.account.dto.AccountResponse;
import co.istad.mobilebanking.feature.account.dto.CreateAccountRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    AccountResponse createAccount(@RequestBody CreateAccountRequest createAccountRequest){
        return accountService.createAccount(createAccountRequest);
    }

    @GetMapping
    List<AccountResponse> getAllAccounts(){
        return accountService.findAllAccount();
    }


}
