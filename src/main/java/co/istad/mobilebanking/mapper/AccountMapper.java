package co.istad.mobilebanking.mapper;

import co.istad.mobilebanking.domain.Account;
import co.istad.mobilebanking.feature.account.dto.AccountResponse;
import co.istad.mobilebanking.feature.account.dto.CreateAccountRequest;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    Account fromCreateAccountRequest(CreateAccountRequest createAccountRequest);
    AccountResponse toAccountResponse(Account account);
    List<AccountResponse> toAccountResponseList(List<Account> accountList);
}
