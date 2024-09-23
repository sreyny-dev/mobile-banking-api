package co.istad.mobilebanking.feature.account;

import co.istad.mobilebanking.feature.account.dto.AccountResponse;
import co.istad.mobilebanking.feature.account.dto.CreateAccountRequest;

import java.util.List;

public interface AccountService {

    AccountResponse createAccount(CreateAccountRequest createAccountRequest);
    List<AccountResponse> findAllAccount();
    AccountResponse findAccountByActNo(String actNo);
    List<AccountResponse> findAccountByPhoneNumber(String phoneNumber);

}
