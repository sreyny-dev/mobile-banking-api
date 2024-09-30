package co.istad.mobilebanking.feature.security;

import co.istad.mobilebanking.domain.User;
import co.istad.mobilebanking.feature.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        //load data from database
        User user=userRepository
                .findByEmail(username)
                .orElseThrow(()->new UsernameNotFoundException(username));

        CustomUserDetails customUserDetails=new CustomUserDetails();
        customUserDetails.setUser(user);

        return customUserDetails;
    }
}
