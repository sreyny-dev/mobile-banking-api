package co.istad.mobilebanking.feature.user;

import co.istad.mobilebanking.domain.Role;
import co.istad.mobilebanking.domain.User;
import co.istad.mobilebanking.feature.role.RoleRepository;
import co.istad.mobilebanking.feature.user.dto.*;
import co.istad.mobilebanking.mapper.UserMapper;
import co.istad.mobilebanking.util.ValidatePhoneNumberUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

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
        user.setIsVerified(false);
        user.setIsAccountNonExpired(true);
        user.setIsAccountNonLocked(true);
        user.setIsCredentialsNonExpired(true);

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
}
