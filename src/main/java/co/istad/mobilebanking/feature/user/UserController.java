package co.istad.mobilebanking.feature.user;

import co.istad.mobilebanking.feature.user.dto.CreateUserRequest;
import co.istad.mobilebanking.feature.user.dto.UserResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    void createUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        userService.createUser(createUserRequest);
    }

    @GetMapping
    List<UserResponse> findAllUser(){
        return userService.findAllUser();
    }
    @GetMapping("/{id}")
    UserResponse findUserById(@PathVariable Integer id){
        return userService.findUserById(id);
    }

}
