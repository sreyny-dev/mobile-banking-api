package co.istad.mobilebanking.feature.auth;

import co.istad.mobilebanking.domain.EmailVerification;
import co.istad.mobilebanking.domain.Role;
import co.istad.mobilebanking.domain.User;
import co.istad.mobilebanking.feature.auth.dto.RegisterRequest;
import co.istad.mobilebanking.feature.auth.dto.VerifyRequest;
import co.istad.mobilebanking.feature.role.RoleRepository;
import co.istad.mobilebanking.feature.user.PasswordResetTokenRepository;
import co.istad.mobilebanking.feature.user.UserRepository;
import co.istad.mobilebanking.mapper.UserMapper;
import co.istad.mobilebanking.util.RandomUtil;
import co.istad.mobilebanking.util.ValidatePhoneNumberUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;



@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    private final EmailVerificationRepository emailVerificationRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;


    @Value("${spring.mail.username}")
    private String adminEmail;

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

        if(!userRepository.existsByEmail(email)){
            throw  new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        User user=userRepository.findByEmail(email).orElseThrow();
        EmailVerification emailVerification=emailVerificationRepository.findByUser(user).orElseThrow();

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
