package co.istad.mobilebanking.feature.user;

import co.istad.mobilebanking.feature.user.dto.*;

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
    //Only Admin/Manager can change pin

}
