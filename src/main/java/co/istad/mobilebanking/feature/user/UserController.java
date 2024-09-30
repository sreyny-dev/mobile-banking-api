package co.istad.mobilebanking.feature.user;

import co.istad.mobilebanking.feature.user.dto.*;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @PostMapping("/create-user")
    @ResponseStatus(HttpStatus.CREATED)
    void createUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        userService.createUser(createUserRequest);
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PostMapping("/create-admin")
    @ResponseStatus(HttpStatus.CREATED)
    void createAdmin(@Valid @RequestBody CreateUserRequest createUserRequest){
        userService.createAdmin(createUserRequest);
    }

    @PreAuthorize("hasAnyRole('MANAGER')")
    @PostMapping("/create-manager")
    @ResponseStatus(HttpStatus.CREATED)
    void createManager(@Valid @RequestBody CreateUserRequest createUserRequest){
        userService.createManager(createUserRequest);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping
    List<UserResponse> findAllUser(){
        return userService.findAllUser();
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/userId/{id}")
    UserResponse findUserById(@PathVariable Integer id){
        return userService.findUserById(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/email/{email}")
    UserResponse findUserByEmail(@PathVariable String email){
        return userService.findUserByEmail(email);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/phoneNumber/{phoneNumber}")
    UserResponse deleteUserByPhoneNumber(@Valid @PathVariable String phoneNumber){
        return userService.deleteUserByPhoneNumber(phoneNumber);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MANAGER')")
    @PutMapping("/update-name")
    UserResponse updateUserName(@Valid @RequestBody UpdateUserRequest updateUserRequest){
        return userService.updateUserName(updateUserRequest);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MANAGER')")
    @PutMapping("/change-password")
    UserResponse changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        return userService.changePassword(changePasswordRequest);
    }

    @PreAuthorize("hasAnyRole('USER','ADMIN', 'MANAGER')")
    @PutMapping("/change-pin")
    UserResponse changePin(@Valid @RequestBody ChangePinRequest changePinRequest){
        return userService.changePin(changePinRequest);
    }


    @PostMapping("/forgot-password/{email}")
    void forgotPassword(@PathVariable String email) throws MessagingException {
        userService.sendPasswordResetLink(email);
    }

    @PostMapping("/reset-password")
    ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword){
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password has been reset successfully!");
    }



}
