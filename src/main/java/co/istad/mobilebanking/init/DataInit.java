package co.istad.mobilebanking.init;

import co.istad.mobilebanking.domain.Role;
import co.istad.mobilebanking.feature.role.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class DataInit {
    private final RoleRepository roleRepository;

    @PostConstruct
    void init(){
        initRoleData();
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

}
