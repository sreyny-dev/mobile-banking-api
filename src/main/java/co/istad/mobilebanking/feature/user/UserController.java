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

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping("/create-user")
    @ResponseStatus(HttpStatus.CREATED)
    void createUser(@Valid @RequestBody CreateUserRequest createUserRequest){
        userService.createUser(createUserRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping("/create-admin")
    @ResponseStatus(HttpStatus.CREATED)
    void createAdmin(@Valid @RequestBody CreateUserRequest createUserRequest){
        userService.createAdmin(createUserRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_MANAGER')")
    @PostMapping("/create-manager")
    @ResponseStatus(HttpStatus.CREATED)
    void createManager(@Valid @RequestBody CreateUserRequest createUserRequest){
        userService.createManager(createUserRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping
    List<UserResponse> findAllUser(){
        return userService.findAllUser();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STAFF','ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/userId/{id}")
    UserResponse findUserById(@PathVariable Integer id){
        return userService.findUserById(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_STAFF','ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/email/{email}")
    UserResponse findUserByEmail(@PathVariable String email){
        return userService.findUserByEmail(email);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/phoneNumber/{phoneNumber}")
    UserResponse deleteUserByPhoneNumber(@Valid @PathVariable String phoneNumber){
        return userService.deleteUserByPhoneNumber(phoneNumber);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/update-name")
    UserResponse updateUserName(@Valid @RequestBody UpdateUserRequest updateUserRequest){
        return userService.updateUserName(updateUserRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PutMapping("/change-password")
    UserResponse changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest){
        return userService.changePassword(changePasswordRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/change-pin")
    UserResponse changePin(@Valid @RequestBody ChangePinRequest changePinRequest){
        return userService.changePin(changePinRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("/forgot-password/{email}")
    void forgotPassword(@PathVariable String email) throws MessagingException {
        userService.sendPasswordResetLink(email);
    }

    @PreAuthorize("hasAuthority('ROLE_CLIENT')")
    @PostMapping("/reset-password")
    ResponseEntity<String> resetPassword(@RequestParam String token, @RequestParam String newPassword){
        userService.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password has been reset successfully!");
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/block/{phoneNumber}")
    void blockUser(@PathVariable String phoneNumber){
        userService.blockedByPhoneNumber(phoneNumber);
    }




}
