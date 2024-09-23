package co.istad.mobilebanking.init;

import co.istad.mobilebanking.domain.Role;
import co.istad.mobilebanking.domain.User;
import co.istad.mobilebanking.feature.role.RoleRepository;
import co.istad.mobilebanking.feature.user.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @PostConstruct
    void init(){
        initRoleData();
        initUser();
    }
    private void initRoleData(){
        ArrayList<Role> roles=new ArrayList<>();
        roles.add(Role.builder().name("USER").build());
        roles.add(Role.builder().name("CLIENT").build());
        roles.add(Role.builder().name("ADMIN").build());
        roles.add(Role.builder().name("STAFF").build());
        roles.add(Role.builder().name("MANAGER").build());

        roleRepository.saveAll(roles);
    }
    private void initUser(){
        User u1=new User();
        u1.setUuid(UUID.randomUUID().toString());
        u1.setName("Alice");
        u1.setPhoneNumber("0972444000");
        u1.setEmail("alice@gmail.com");
        u1.setPassword("444444");
        u1.setDob(LocalDate.ofEpochDay(2000-10-21));
        u1.setConfirmPassword("4444444");
        u1.setPin("3333");
        u1.setGender("Female");
        u1.setCreatedAt(LocalDateTime.now());

        List<Role> roles=new ArrayList<>();
        roles.add(roleRepository.findById(1).orElseThrow());
        u1.setRoles(roles);

        u1.setIsAccountNonExpired(true);
        u1.setIsAccountNonLocked(true);
        u1.setIsCredentialsNonExpired(true);
        u1.setIsDeleted(false);
        u1.setIsBlocked(false);
        u1.setIsVerified(true);

        userRepository.save(u1);
    }

}
