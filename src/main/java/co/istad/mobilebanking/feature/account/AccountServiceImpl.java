package co.istad.mobilebanking.feature.account;

import co.istad.mobilebanking.domain.Account;
import co.istad.mobilebanking.domain.AccountType;
import co.istad.mobilebanking.domain.User;
import co.istad.mobilebanking.domain.UserAccount;
import co.istad.mobilebanking.feature.accounType.AccountTypeRepository;
import co.istad.mobilebanking.feature.account.dto.AccountResponse;
import co.istad.mobilebanking.feature.account.dto.CreateAccountRequest;
import co.istad.mobilebanking.feature.user.UserRepository;
import co.istad.mobilebanking.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService{

    private final AccountRepository accountRepository;
    private final UserAccountRepository userAccountRepository;
    private final AccountMapper accountMapper;
    private final AccountTypeRepository accountTypeRepository;
    private final UserRepository userRepository;

    @Override
    public AccountResponse createAccount(CreateAccountRequest createAccountRequest) {

        Account account=accountMapper.fromCreateAccountRequest(createAccountRequest);

        AccountType accountType=accountTypeRepository.findByAlias("saving-account").orElseThrow();
        account.setAccountType(accountType);
        account.setTransferLimit(BigDecimal.valueOf(5000));
        account.setCreatedAt(LocalDateTime.now());
        account.setIsHidden(false);
        account.setIsDeleted(false);

        //create userAccount out of account

        //find user

        User user=userRepository.findByPhoneNumber(createAccountRequest.phoneNumber()).orElseThrow();

        UserAccount userAccount=new UserAccount();
        userAccount.setAccount(account);
        userAccount.setUser(user);
        userAccount.setCreatedAt(LocalDateTime.now());
        userAccount.setIsBlocked(false);
        userAccount.setIsDeleted(false);

        userAccountRepository.save(userAccount);
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public List<AccountResponse> findAllAccount() {
        List<Account> accounts=accountRepository.findAll();
        return accountMapper.toAccountResponseList(accounts);
    }
}
