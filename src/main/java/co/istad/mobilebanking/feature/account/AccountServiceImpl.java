package co.istad.mobilebanking.feature.account;

import co.istad.mobilebanking.domain.Account;
import co.istad.mobilebanking.domain.AccountType;
import co.istad.mobilebanking.domain.User;
import co.istad.mobilebanking.domain.UserAccount;
import co.istad.mobilebanking.feature.accounType.AccountTypeRepository;
import co.istad.mobilebanking.feature.account.dto.AccountResponse;
import co.istad.mobilebanking.feature.account.dto.CreateAccountRequest;
import co.istad.mobilebanking.feature.account.dto.UpdateAccountRequest;
import co.istad.mobilebanking.feature.user.UserRepository;
import co.istad.mobilebanking.mapper.AccountMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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

        //Validate phoneNumber
        if(!userRepository.existsByPhoneNumber(createAccountRequest.phoneNumber())){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Phone Number does not exist");
        }
        //Validate act No
        if(accountRepository.existsByActNo(createAccountRequest.actNo())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Account number is already existed");
        }
        //validate account type alias

        if(!accountTypeRepository.existsByAlias(createAccountRequest.accountTypeAlias())){
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Account Type is not correct");
        }

        Account account=accountMapper.fromCreateAccountRequest(createAccountRequest);

        AccountType accountType=accountTypeRepository.findByAlias(createAccountRequest.accountTypeAlias()).orElseThrow();
        account.setAccountType(accountType);
        account.setTransferLimit(BigDecimal.valueOf(5000));
        account.setCreatedAt(LocalDateTime.now());
        account.setIsHidden(false);
        account.setIsDeleted(false);

        //create userAccount out of account
        //find user
        User user=userRepository
                .findByPhoneNumber(createAccountRequest.phoneNumber())
                .orElseThrow();

        UserAccount userAccount=new UserAccount();
        userAccount.setAccount(account);
        userAccount.setUser(user);
        userAccount.setCreatedAt(LocalDateTime.now());
        userAccount.setIsBlocked(false);
        userAccount.setIsDeleted(false);

        account.setUserAccount(userAccount);

        userAccountRepository.save(userAccount);

        return accountMapper.toAccountResponse(account);
    }

    @Override
    public List<AccountResponse> findAllAccount() {
        List<Account> accounts=accountRepository.findAll();
        return accountMapper.toAccountResponseList(accounts);
    }

    @Override
    public AccountResponse findAccountByActNo(String actNo) {
        Account account=accountRepository.findByActNo(actNo)
                .orElseThrow();
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public List<AccountResponse> findAccountByPhoneNumber(String phoneNumber) {
        List<Account> accounts=accountRepository.findAccountsByPhoneNumber(phoneNumber);
        if(accounts.isEmpty()){
            throw new ResponseStatusException(
              HttpStatus.NOT_FOUND, "No account for this phone number"
            );
        }
        return accountMapper.toAccountResponseList(accounts);
    }

    @Override
    public AccountResponse updateAccountAlias(UpdateAccountRequest updateAccountRequest) {

        Account account=accountRepository
                .findByActNo(updateAccountRequest.actNo())
                .orElseThrow();
        account.setAlias(updateAccountRequest.alias());

        accountRepository.save(account);
        return accountMapper.toAccountResponse(account);
    }

    @Override
    public AccountResponse hideAccount(String actNo) {

        Account account=accountRepository
                .findByActNo(actNo)
                .orElseThrow();
        account.setIsHidden(true);
        accountRepository.save(account);

        return accountMapper.toAccountResponse(account);
    }

    @Override
    public AccountResponse deleteAccount(String actNo) {

        Account account=accountRepository
                .findByActNo(actNo)
                .orElseThrow();
        account.setIsDeleted(true);

        accountRepository.save(account);

        return accountMapper.toAccountResponse(account);
    }
}
