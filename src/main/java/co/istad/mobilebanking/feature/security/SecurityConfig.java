package co.istad.mobilebanking.feature.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;
    //to work with dao and security
    @Bean
    DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider auth=new DaoAuthenticationProvider();
        auth.setUserDetailsService(userDetailsService);
        auth.setPasswordEncoder(passwordEncoder);
        return auth;
    }


    @Bean
    SecurityFilterChain configFilterChain(HttpSecurity http) throws Exception {

//        http.authorizeHttpRequests(endpoint-> endpoint
//                .requestMatchers(HttpMethod.GET,"/api/v1/accounts").permitAll()
//                .requestMatchers(HttpMethod.POST,"/api/v1/accounts").hasRole("USER")
//                .anyRequest().authenticated());

        http.httpBasic(Customizer.withDefaults());

        http.csrf(token -> token.disable());

        //when we disable csrf toke, in order to secure api, we need to turn api to stateless, new session, new csrf token

        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }


}


//CREATE verification code
//1. create app password in gmail: to connect google with our spring applicaiton
//2.