package co.istad.mobilebanking.init;

import co.istad.mobilebanking.domain.*;
import co.istad.mobilebanking.feature.accounType.AccountTypeRepository;
import co.istad.mobilebanking.feature.account.AccountRepository;
import co.istad.mobilebanking.feature.account.UserAccountRepository;
import co.istad.mobilebanking.feature.card.CardTypeRepository;
import co.istad.mobilebanking.feature.role.RoleRepository;
import co.istad.mobilebanking.feature.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final AccountTypeRepository accountTypeRepository;
    private final UserAccountRepository userAccountRepository;
    private final CardTypeRepository cardTypeRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${file-server.base-uri}")
    private String baseUri;

    @PostConstruct
    void init(){
        initRoleData();
        initCardType();
        initAccountType();
        initUser();
        initAccount();
    }


    private void initCardType(){
        CardType master=new CardType();
        master.setName("master card");
        master.setAlias("master-card");
        master.setIsDeleted(false);

        CardType visa=new CardType();
        visa.setName("visa card");
        visa.setAlias("visa-card");
        visa.setIsDeleted(false);

        cardTypeRepository.saveAll(List.of(master, visa));

    }

    private void initAccount(){

        User user1 = userRepository.findByPhoneNumber("0964335400").orElseThrow();
        UserAccount userAccount1 = new UserAccount();
        userAccount1.setUser(user1);
        userAccount1.setCreatedAt(LocalDateTime.now());
        userAccount1.setIsBlocked(false);
        userAccount1.setIsDeleted(false);

        Account a1=new Account();
        a1.setActNo("000111222");
        a1.setAlias("nyny-saving-account");
        a1.setAccountType(accountTypeRepository.findByAlias("saving-account").orElseThrow());
        a1.setTransferLimit(BigDecimal.valueOf(5000));
        a1.setBalance(BigDecimal.valueOf(500));
        a1.setUserAccount(userAccount1);
        a1.setCreatedAt(LocalDateTime.now());
        a1.setIsHidden(false);
        a1.setIsDeleted(false);

        userAccount1.setAccount(a1);


        User user2 = userRepository.findByPhoneNumber("0972444000").orElseThrow();
        UserAccount userAccount2 = new UserAccount();
        userAccount2.setUser(user2);
        userAccount2.setCreatedAt(LocalDateTime.now());
        userAccount2.setIsBlocked(false);
        userAccount2.setIsDeleted(false);

        Account a2=new Account();
        a2.setActNo("000674000");
        a2.setAlias("dara-payroll-account");
        a2.setAccountType(accountTypeRepository.findByAlias("payroll-account").orElseThrow());
        a2.setTransferLimit(BigDecimal.valueOf(5000));
        a2.setBalance(BigDecimal.valueOf(1000));
        a2.setUserAccount(userAccount2);
        a2.setIsHidden(false);
        a2.setIsDeleted(false);
        a2.setCreatedAt(LocalDateTime.now());

        userAccount2.setAccount(a2);

        accountRepository.saveAll(List.of(a1, a2));
        userAccountRepository.saveAll(List.of(userAccount1, userAccount2));

    }
    private void initAccountType(){
        AccountType t1=new AccountType();
        t1.setName("saving account");
        t1.setAlias("saving-account");
        t1.setIsDeleted(false);
        AccountType t2=new AccountType();
        t2.setName("payroll account");
        t2.setAlias("payroll-account");
        t2.setIsDeleted(false);
        AccountType t3=new AccountType();
        t3.setName("current account");
        t3.setAlias("current-account");
        t3.setIsDeleted(false);
        AccountType t4=new AccountType();
        t4.setName("fix deposit account");
        t4.setAlias("fix-deposit-account");
        t4.setIsDeleted(false);

        accountTypeRepository.saveAll(List.of(t1,t2,t3,t4));

    }
    private void initRoleData(){
        ArrayList<Role> roles=new ArrayList<>();
        roles.add(Role.builder().name("USER").build());
        roles.add(Role.builder().name("CLIENT").build());
        roles.add(Role.builder().name("STAFF").build());
        roles.add(Role.builder().name("ADMIN").build());
        roles.add(Role.builder().name("MANAGER").build());

        roleRepository.saveAll(roles);
    }
    private void initUser(){
        User u1=new User();
        u1.setUuid(UUID.randomUUID().toString());
        u1.setName("Alice");
        u1.setPhoneNumber("0972444000");
        u1.setEmail("alice@gmail.com");
        u1.setPassword(passwordEncoder.encode("1111"));
        u1.setDob(LocalDate.ofEpochDay(2000-10-21));
        u1.setConfirmPassword(passwordEncoder.encode("1111"));
        u1.setPin("3333");
        u1.setGender("Female");
        u1.setCreatedAt(LocalDateTime.now());
        u1.setProfilePicture(baseUri+"default.png");

        List<Role> roleU1=new ArrayList<>();
        roleU1.add(roleRepository.findById(1).orElseThrow());
        roleU1.add(roleRepository.findById(4).orElseThrow());
        u1.setRoles(roleU1);

        u1.setIsAccountNonExpired(true);
        u1.setIsAccountNonLocked(true);
        u1.setIsCredentialsNonExpired(true);
        u1.setIsDeleted(false);
        u1.setIsBlocked(false);
        u1.setIsVerified(true);

        User u2=new User();
        u2.setUuid(UUID.randomUUID().toString());
        u2.setName("sreyny");
        u2.setPhoneNumber("0964335400");
        u2.setEmail("thasreyny9@gmail.com");
        u2.setPassword(passwordEncoder.encode("qwer"));
        u2.setDob(LocalDate.ofEpochDay(2000-10-21));
        u2.setConfirmPassword(passwordEncoder.encode("qwer"));
        u2.setPin("3333");
        u2.setGender("Female");
        u2.setCreatedAt(LocalDateTime.now());
        u2.setProfilePicture(baseUri+"default.png");

        List<Role> roleU2=new ArrayList<>();
        roleU2.add(roleRepository.findById(1).orElseThrow());
        roleU2.add(roleRepository.findById(2).orElseThrow());
        u2.setRoles(roleU2);

        u2.setIsAccountNonExpired(true);
        u2.setIsAccountNonLocked(true);
        u2.setIsCredentialsNonExpired(true);
        u2.setIsDeleted(false);
        u2.setIsBlocked(false);
        u2.setIsVerified(true);

        User u3=new User();
        u3.setUuid(UUID.randomUUID().toString());
        u3.setName("THA SREYNY");
        u3.setPhoneNumber("096223344");
        u3.setEmail("sreyny@gmail.com");
        u3.setPassword(passwordEncoder.encode("qwer"));
        u3.setDob(LocalDate.ofEpochDay(2000-10-21));
        u3.setConfirmPassword(passwordEncoder.encode("qwer"));
        u3.setPin("3333");
        u3.setGender("Female");
        u3.setCreatedAt(LocalDateTime.now());
        List<Role> roleU3=new ArrayList<>();
        roleU3.add(roleRepository.findById(1).orElseThrow());
        roleU3.add(roleRepository.findById(5).orElseThrow());
        u3.setRoles(roleU3);
        u3.setProfilePicture(baseUri+"default.png");

        u3.setIsAccountNonExpired(true);
        u3.setIsAccountNonLocked(true);
        u3.setIsCredentialsNonExpired(true);
        u3.setIsDeleted(false);
        u3.setIsBlocked(false);
        u3.setIsVerified(true);

        userRepository.saveAll(List.of(u1,u2,u3));
    }

}
