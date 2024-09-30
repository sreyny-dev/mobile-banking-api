package co.istad.mobilebanking.feature.user;

import co.istad.mobilebanking.domain.PasswordReset;
import co.istad.mobilebanking.domain.Role;
import co.istad.mobilebanking.domain.User;
import co.istad.mobilebanking.feature.role.RoleRepository;
import co.istad.mobilebanking.feature.user.dto.*;
import co.istad.mobilebanking.mapper.UserMapper;
import co.istad.mobilebanking.util.ValidatePhoneNumberUtil;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String adminMail;


    //CREATE User by admin or manager
    @Override
    public void createUser(CreateUserRequest createUserRequest) {

        //validate phone number if it is already existed
        if(userRepository.existsByPhoneNumber(createUserRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone Number already exists");
        }
        //validate email
        if(userRepository.existsByEmail(createUserRequest.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        //validate national ID
        if(userRepository.existsByNationalCardId(createUserRequest.nationalCardId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "National Id card is already in use");
        }

        //validate student ID
        if(userRepository.existsByStudentCardId(createUserRequest.studentCardId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student Card is already in use");
        }

        //validate pw

        if(!createUserRequest.password().equals(createUserRequest.confirmPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password does not match");
        }

        //phone number cannot contain character
        if(!ValidatePhoneNumberUtil.phoneNumNonChar(createUserRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number cannot contain Characters");
        }

        User user=userMapper.fromCreateUserRequest(createUserRequest);

        ArrayList<Role> roles=new ArrayList<>();
        roles.add(roleRepository.findById(1).orElseThrow());
        user.setUuid(UUID.randomUUID().toString());
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setIsVerified(true);
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));

        userRepository.save(user);

    }

    @Override
    public List<UserResponse> findAllUser() {

        List<User> users=userRepository.findAll();

        return userMapper.toUserResponse(users);
    }

    @Override
    public UserResponse findUserById(Integer id) {
        User user=userRepository.findById(id).orElseThrow();
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse findUserByEmail(String email) {

        User user=userRepository.findByEmail(email).orElseThrow();
        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse deleteUserByPhoneNumber(String phoneNumber) {

        User user=userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow();
        user.setIsDeleted(true);

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse updateUserName(UpdateUserRequest updateUserRequest) {

        User user=userRepository
                .findByPhoneNumber(updateUserRequest.phoneNumber())
                .orElseThrow();
        user.setName(updateUserRequest.name());
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse changePassword(ChangePasswordRequest changePasswordRequest) {

        //validate phone number
        if(!userRepository.existsByPhoneNumber(changePasswordRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        //validate new and confirm new password
        if(!changePasswordRequest.newPassword().equals(changePasswordRequest.confirmNewPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New Password and its confirm password does not match!");
        }
        User user=userRepository
                .findByPhoneNumber(changePasswordRequest.phoneNumber())
                .orElseThrow();

        if(changePasswordRequest.newPassword().equals(user.getPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New Password and its old password are the same!");
        }

        //validate input password and old password
        if(changePasswordRequest.oldPassword().equals(user.getPassword())){
            user.setPassword(changePasswordRequest.newPassword());

        }
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public UserResponse changePin(ChangePinRequest changePinRequest) {
        //validate phone number
        if(!userRepository.existsByPhoneNumber(changePinRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User does not exist");
        }

        User user=userRepository
                .findByPhoneNumber(changePinRequest.phoneNumber())
                .orElseThrow();

        if(user.getPin().equals(changePinRequest.newPin())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "New Pin and its old Pin are the same!");
        }

        user.setPin(changePinRequest.newPin());

        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    @Override
    public void createAdmin(CreateUserRequest createUserRequest) {
        //validate phone number if it is already existed
        if(userRepository.existsByPhoneNumber(createUserRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone Number already exists");
        }
        //validate email
        if(userRepository.existsByEmail(createUserRequest.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        //validate national ID
        if(userRepository.existsByNationalCardId(createUserRequest.nationalCardId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "National Id card is already in use");
        }

        //validate student ID
        if(userRepository.existsByStudentCardId(createUserRequest.studentCardId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student Card is already in use");
        }

        //validate pw

        if(!createUserRequest.password().equals(createUserRequest.confirmPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password does not match");
        }

        //phone number cannot contain character
        if(!ValidatePhoneNumberUtil.phoneNumNonChar(createUserRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number cannot contain Characters");
        }

        User user=userMapper.fromCreateUserRequest(createUserRequest);

        ArrayList<Role> roles=new ArrayList<>();
        roles.add(roleRepository.findById(1).orElseThrow());
        roles.add(roleRepository.findById(4).orElseThrow());
        user.setUuid(UUID.randomUUID().toString());
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setIsVerified(true);
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));

        userRepository.save(user);
    }

    @Override
    public void createManager(CreateUserRequest createUserRequest) {
        //validate phone number if it is already existed
        if(userRepository.existsByPhoneNumber(createUserRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Phone Number already exists");
        }
        //validate email
        if(userRepository.existsByEmail(createUserRequest.email())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email already exists");
        }
        //validate national ID
        if(userRepository.existsByNationalCardId(createUserRequest.nationalCardId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "National Id card is already in use");
        }

        //validate student ID
        if(userRepository.existsByStudentCardId(createUserRequest.studentCardId())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Student Card is already in use");
        }

        //validate pw

        if(!createUserRequest.password().equals(createUserRequest.confirmPassword())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password does not match");
        }

        //phone number cannot contain character
        if(!ValidatePhoneNumberUtil.phoneNumNonChar(createUserRequest.phoneNumber())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Phone Number cannot contain Characters");
        }

        User user=userMapper.fromCreateUserRequest(createUserRequest);

        ArrayList<Role> roles=new ArrayList<>();
        roles.add(roleRepository.findById(1).orElseThrow());
        roles.add(roleRepository.findById(5).orElseThrow());
        user.setUuid(UUID.randomUUID().toString());
        user.setRoles(roles);
        user.setCreatedAt(LocalDateTime.now());
        user.setIsBlocked(false);
        user.setIsDeleted(false);
        user.setIsVerified(true);
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(user.getConfirmPassword()));

        userRepository.save(user);
    }

    @Override
    public void sendPasswordResetLink(String email) throws MessagingException {

        User user=userRepository
                .findByEmail(email)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        PasswordReset token=new PasswordReset();
        token.setToken(UUID.randomUUID().toString());
        token.setUser(user);
        token.setExpiredAt(LocalTime.now().plusMinutes(60));
        passwordResetTokenRepository.save(token);

        sendResetPasswordEmail(user, token.getToken());

    }

    @Override
    public void sendResetPasswordEmail(User user, String token) throws MessagingException {
        String resetLink="http://localhost:8080/api/v1/users/reset-password?token="+token;
        MimeMessage mimeMessage=javaMailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(mimeMessage);

        helper.setTo(user.getEmail());
        helper.setFrom(adminMail);
        helper.setSubject("Reset Your Password");
        helper.setText("<p>Hello " + user.getEmail() + ",</p>"
                + "<p>Click the link below to reset your password:</p>"
                + "<p><a href=\"" + resetLink + "\">Reset Password</a></p>"
                + "<p>If you did not request this, please ignore this email.</p>", true);
        javaMailSender.send(mimeMessage);

    }

    @Override
    public void resetPassword(String token, String newPassword) {

        PasswordReset resetToken=passwordResetTokenRepository
                .findByToken(token)
                .orElseThrow(()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Token does not exist!"));

        if(resetToken.getExpiredAt().isBefore(LocalTime.now())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "token has expired");
        }

        User user=resetToken.getUser();
//        user.setPassword(newPassword);
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);

    }
}
