package co.istad.mobilebanking.feature.user;

import co.istad.mobilebanking.domain.User;
import co.istad.mobilebanking.feature.user.dto.*;
import jakarta.mail.MessagingException;

import java.util.List;

public interface UserService {
    void createUser(CreateUserRequest createUserRequest);
    List<UserResponse> findAllUser();
    UserResponse findUserById(Integer id);
    UserResponse findUserByEmail(String email);
    UserResponse deleteUserByPhoneNumber(String PhoneNumber);
    UserResponse updateUserName(UpdateUserRequest updateUserRequest);
    UserResponse changePassword(ChangePasswordRequest changePasswordRequest);
    UserResponse changePin(ChangePinRequest changePinRequest);
    void createAdmin(CreateUserRequest createUserRequest);
    void createManager(CreateUserRequest createUserRequest);
    //Only Admin/Manager can change pin

    void sendPasswordResetLink(String email) throws MessagingException;
    void sendResetPasswordEmail(User user, String token) throws MessagingException;
    void resetPassword(String token, String newPassword);

}
