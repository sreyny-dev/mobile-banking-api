package co.istad.mobilebanking.feature.account;

import co.istad.mobilebanking.feature.account.dto.AccountResponse;
import co.istad.mobilebanking.feature.account.dto.CreateAccountRequest;
import co.istad.mobilebanking.feature.account.dto.UpdateAccountRequest;

import java.util.List;

public interface AccountService {

    AccountResponse createAccount(CreateAccountRequest createAccountRequest);
    List<AccountResponse> findAllAccount();
    AccountResponse findAccountByActNo(String actNo);
    List<AccountResponse> findAccountByPhoneNumber(String phoneNumber);
    AccountResponse updateAccountAlias(UpdateAccountRequest updateAccountRequest);
    AccountResponse hideAccount(String actNo);
    AccountResponse deleteAccount(String actNo);
}
