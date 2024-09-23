package co.istad.mobilebanking.feature.user;

import co.istad.mobilebanking.feature.user.dto.CreateUserRequest;
import co.istad.mobilebanking.feature.user.dto.UpdateUserRequest;
import co.istad.mobilebanking.feature.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    void createUser(CreateUserRequest createUserRequest);
    List<UserResponse> findAllUser();
    UserResponse findUserById(Integer id);
    UserResponse findUserByEmail(String email);
    UserResponse deleteUserByPhoneNumber(String PhoneNumber);
    UserResponse updateUserName(UpdateUserRequest updateUserRequest);
}
