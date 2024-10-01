package co.istad.mobilebanking.feature.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.stream.Collectors;

@EnableMethodSecurity
@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final PasswordEncoder passwordEncoder;

    private final UserDetailsService userDetailsService;

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
            String id = jwt.getId();
            log.info("ID: {}", id);
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(id);
            log.info("AUTHORITIES: {}", userDetails.getAuthorities());
            return userDetails.getAuthorities()
                    .stream()
                    .map(grantedAuthority -> new SimpleGrantedAuthority(grantedAuthority.getAuthority()))
                    .collect(Collectors.toList());
        };

        var jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    JwtAuthenticationProvider jwtAuthenticationProvider(@Qualifier("jwtDecoderRefreshToken") JwtDecoder jwtDecoderRefreshToken) {
        JwtAuthenticationProvider provider = new JwtAuthenticationProvider(jwtDecoderRefreshToken);
        provider.setJwtAuthenticationConverter(jwtAuthenticationConverter());
        return provider;
    }



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
//        http.httpBasic(Customizer.withDefaults());

        //security with jwt
        http.oauth2ResourceServer(jwt->jwt.jwt(Customizer.withDefaults()));

        http.csrf(token -> token.disable());

        //when we disable csrf toke, in order to secure api, we need to turn api to stateless, new session, new csrf token
        http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }


}

//CREATE verification code
//1. create app password in gmail: to connect google with our spring applicaiton
//2.