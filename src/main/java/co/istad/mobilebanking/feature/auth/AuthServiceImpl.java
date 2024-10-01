package co.istad.mobilebanking.feature.auth;

import co.istad.mobilebanking.domain.EmailVerification;
import co.istad.mobilebanking.domain.Role;
import co.istad.mobilebanking.domain.User;
import co.istad.mobilebanking.feature.auth.dto.*;
import co.istad.mobilebanking.feature.role.RoleRepository;
import co.istad.mobilebanking.feature.user.UserRepository;
import co.istad.mobilebanking.mapper.UserMapper;
import co.istad.mobilebanking.util.RandomUtil;
import co.istad.mobilebanking.util.ValidatePhoneNumberUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationProvider;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final EmailVerificationRepository emailVerificationRepository;
    private final DaoAuthenticationProvider daoAuthenticationProvider;
    private final JwtEncoder accessTokenjwtEncoder;


    @Value("${spring.mail.username}")
    private String adminEmail;

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private JwtEncoder jwtEncoderRefreshToken;

    @Autowired
    @Qualifier("jwtEncoderRefreshToken")
    public void setJwtEncoderRefreshToken(JwtEncoder jwtEncoderRefreshToken) {
        this.jwtEncoderRefreshToken = jwtEncoderRefreshToken;
    }


    @Override
    public JwtResponse login(LoginRequest loginRequest) {

        Authentication auth= new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        //UsernamePassAuthe invoke dao in security config
        auth=daoAuthenticationProvider.authenticate(auth);
        log.info("Auth: {}", auth.getPrincipal());

        //get scope or authority
        String scope=auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());
        log.info("scope: {}", scope);

        //generate jwt token jwtEncoder
        //1. define ClaimSet/Payload

        Instant now= Instant.now();

        JwtClaimsSet jwtClaimsSet=JwtClaimsSet.builder()
                .id(auth.getName())
                .subject("Access API")
                .issuer(auth.getName())
                .issuedAt(now)
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .audience(List.of("NextJS", "JavaScript", "Android"))
                .claim("scope", scope)
                .claim("isAdmin", true)
                .claim("studentId", "12113053")
                .build();
        //2.generate token
        String accessToken=accessTokenjwtEncoder
                .encode(JwtEncoderParameters.from(jwtClaimsSet))
                .getTokenValue();
        log.info("Access Token: {}", accessToken);


        //JWT Claimset for refresh token
        JwtClaimsSet jwtClaimSetRefreshToken=JwtClaimsSet
                .builder()
                .id(auth.getName())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("NextJS", "JavaScript", "Android"))
                .subject("Refresh Token")
                .expiresAt(now.plus(7, ChronoUnit.DAYS))
                .build();

        JwtEncoderParameters jwtEncoderParametersRefreshToken=JwtEncoderParameters.from(jwtClaimSetRefreshToken);
        Jwt jwtRefreshToken=jwtEncoderRefreshToken.encode(jwtEncoderParametersRefreshToken);
        String refreshToken= jwtRefreshToken.getTokenValue();

        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .build();
    }

    @Override
    public JwtResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        Authentication auth=new BearerTokenAuthenticationToken(refreshTokenRequest.refreshToken());

        auth=jwtAuthenticationProvider.authenticate(auth);
        String scope=auth.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining());
        log.info("new scope: {}", scope);
        log.info("auth: {}", auth);
        Instant now= Instant.now();
        //convert auth to jwt
        Jwt jwt=(Jwt) auth.getPrincipal();

       //create access token claim set
        JwtClaimsSet jwtClaimsSet=JwtClaimsSet
                .builder()
                .id(jwt.getId())
                .issuedAt(now)
                .issuer("web")
                .audience(List.of("NextJS", "JavaScript", "Android"))
                .expiresAt(now.plus(30, ChronoUnit.MINUTES))
                .claim("scope", scope)
                .build();
        JwtEncoderParameters jwtEncoderParameters=JwtEncoderParameters.from(jwtClaimsSet);
        Jwt encodedJwt=accessTokenjwtEncoder.encode(jwtEncoderParameters);

        String accessToken=encodedJwt.getTokenValue();
        String refreshToken=refreshTokenRequest.refreshToken();

        if(Duration.between(Instant.now(), jwt.getExpiresAt()).toDays()<1){
            JwtClaimsSet jwtClaimsSetRefreshToken=JwtClaimsSet
                    .builder()
                    .id(auth.getName())
                    .issuedAt(now)
                    .audience(List.of("nextJS", "JavaScript", "Android"))
                    .subject("Refrsh Token")
                    .expiresAt(now.plus(7, ChronoUnit.DAYS))
                    .build();
            JwtEncoderParameters jwtEncoderParametersRefreshToken=JwtEncoderParameters.from(jwtClaimsSetRefreshToken);
            Jwt jwtRefreshToken = jwtEncoderRefreshToken.encode(jwtEncoderParametersRefreshToken);
            refreshToken = jwtRefreshToken.getTokenValue();
        }



        return JwtResponse.builder()
                .tokenType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    @Override
    public void register(RegisterRequest registerRequest) throws MessagingException {

        if(userRepository.existsByPhoneNumber(registerRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone Number already exists");
        }
        //validate email
        if(userRepository.existsByEmail(registerRequest.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        //validate national ID
        if(userRepository.existsByNationalCardId(registerRequest.nationalCardId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "National Id card is already in use");
        }

        //validate student ID
        if(userRepository.existsByStudentCardId(registerRequest.studentCardId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student Card is already in use");
        }

        //validate pw

        if(!registerRequest.password().equals(registerRequest.confirmPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password does not match");
        }

        //phone number cannot contain character
        if(!ValidatePhoneNumberUtil.phoneNumNonChar(registerRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number cannot contain Characters");
        }

        User user=userMapper.fromRegisterRequest(registerRequest);

        ArrayList<Role> roles=new ArrayList<>();
        roles.add(roleRepository.findById(1).orElseThrow());
        user.setUuid(UUID.randomUUID().toString());
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());
        user.setIsBlocked(false);
        user.setIsDeleted(true);
        user.setIsVerified(false);
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));

        user= userRepository.save(user);

        //After save user to db, send mail to verification code
        //STEP1. prepare email verification data
        EmailVerification emailVerification=new EmailVerification();
        emailVerification.setVerificationCode(RandomUtil.generateCode());
        emailVerification.setExpiredAt(LocalTime.now().plusMinutes(1));
        emailVerification.setUser(user);

// Step 2: send mail
        sendEmail(user, emailVerification.getVerificationCode());
        emailVerificationRepository.save(emailVerification);

    }


    @Override
    public void resendVerificationCode(String email) throws MessagingException {

        if (!userRepository.existsByEmail(email)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        User user = userRepository.findByEmail(email).orElseThrow();
        EmailVerification emailVerification = emailVerificationRepository.findByUser(user).orElseThrow();

        emailVerification.setVerificationCode(RandomUtil.generateCode());
        emailVerification.setExpiredAt(LocalTime.now().plusMinutes(1));
        emailVerification.setUser(user);

        sendEmail(user, emailVerification.getVerificationCode());
        emailVerificationRepository.save(emailVerification);

    }

    @Override
    public void verify(VerifyRequest verifyRequest) {


        if(!userRepository.existsByEmail(verifyRequest.email())){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }
        User user=userRepository.findByEmail(verifyRequest.email()).orElseThrow();


        EmailVerification emailVerification=emailVerificationRepository
                .findByUser(user)
                .orElseThrow();

        if(!verifyRequest.verificationCode().equals(emailVerification.getVerificationCode())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong verification code");
        }

        if(LocalTime.now().isAfter(emailVerification.getExpiredAt())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Verification code is already expired.");
        }

        user.setIsVerified(true);
        user.setIsDeleted(false);

        List<Role> roles=new ArrayList<>();
        roles.add(roleRepository.findById(1).orElseThrow());
        roles.add(roleRepository.findById(2).orElseThrow());

        user.setRoles(roles);

        userRepository.save(user);

    }


    private void sendEmail(User user, String verificationCode) throws MessagingException {

        MimeMessage mimeMailMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMailMessage, true, "UTF-8");

        String myHtml = """
            <html>
                <head>
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f4f4f4; padding: 20px; text-align: center; }
                        .container { max-width: 600px; margin: 0 auto; background: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); }
                        .header { text-align: center; padding: 10px 0; }
                        .header h1 { color: #333; }
                        .content { margin: 20px 0; text-align: center; }
                        .verification-code { font-size: 24px; font-weight: bold; color: #007bff; text-align: center; margin: 20px auto; }
                        .footer { margin-top: 20px; text-align: center; font-size: 12px; color: #777; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1>Mobile Banking Verification</h1>
                        </div>
                        <div class="content">
                            <p>Hello <strong>%s</strong>,</p>
                            <p>We have resent your verification code. Please verify your email address by using the code below:</p>
                            <p class="verification-code">%s</p>
                            <p>This code will expire in 1 minute. If you did not request this, please ignore this email.</p>
                        </div>
                        <div class="footer">
                            <p>Best regards,</p>
                            <p>The Mobile Banking Team</p>
                        </div>
                    </div>
                </body>
            </html>
            """.formatted(user.getEmail(), verificationCode);

        helper.setTo(user.getEmail());
        helper.setFrom(adminEmail);
        helper.setSubject("Email Verification - Mobile Banking API");
        helper.setText(myHtml, true);

        javaMailSender.send(mimeMailMessage);

    }


}
