package co.istad.mobilebanking.feature.account;

import co.istad.mobilebanking.feature.account.dto.AccountResponse;
import co.istad.mobilebanking.feature.account.dto.CreateAccountRequest;
import co.istad.mobilebanking.feature.account.dto.UpdateAccountRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    AccountResponse createAccount(@Valid @RequestBody CreateAccountRequest createAccountRequest){
        return accountService.createAccount(createAccountRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping
    List<AccountResponse> getAllAccounts(){
        return accountService.findAllAccount();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/actNo/{actNo}")
    AccountResponse findAccountByActNo(@PathVariable String actNo){
        return accountService.findAccountByActNo(actNo);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("phoneNumber/{phoneNumber}")
    List<AccountResponse> findAccountByPhoneNumber(@PathVariable String phoneNumber){
        return accountService.findAccountByPhoneNumber(phoneNumber);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/update-alias")
    AccountResponse updateAccountAlias(@Valid @RequestBody UpdateAccountRequest updateAccountRequest){
        return accountService.updateAccountAlias(updateAccountRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_CLIENT', 'ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/hide-account/{actNo}")
    AccountResponse hideAccount(@PathVariable String actNo){
        return accountService.hideAccount(actNo);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/delete-account/{actNo}")
    AccountResponse deleteAccount(@PathVariable String actNo){
        return accountService.deleteAccount(actNo);
    }

}
