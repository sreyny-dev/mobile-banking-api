package co.istad.mobilebanking.mapper;

import co.istad.mobilebanking.domain.User;
import co.istad.mobilebanking.feature.auth.dto.RegisterRequest;
import co.istad.mobilebanking.feature.user.dto.CreateUserRequest;
import co.istad.mobilebanking.feature.user.dto.UserResponse;
import org.mapstruct.Mapper;

import java.util.List;

//@Mapper(componentModel = "spring")
//public interface UserMapper {
//    User fromCreateUserRequest(CreateUserRequest createUserRequest);
//}
@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromCreateUserRequest(CreateUserRequest createUserRequest);
    List<UserResponse> toUserResponse(List<User> users);
    UserResponse toUserResponse(User user);
    User fromRegisterRequest(RegisterRequest registerRequest);

}
