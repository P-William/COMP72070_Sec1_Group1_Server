package com.group11.accord.app.controllers;

import com.group11.accord.api.user.Account;
import com.group11.accord.api.user.NewAccount;
import com.group11.accord.app.services.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
@Tag(name = "Authorization Controller", description = "Handles all operations regarding Authorization")
@RequiredArgsConstructor
public class AuthorizationController {
    AuthorizationService authorizationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    @Operation(summary = "Create a new user account")
    public Account createAccount(@RequestBody @NotNull @Valid NewAccount newAccount){
        return authorizationService.createAccount(newAccount);
    }

    @PostMapping("/login")
    @Operation(summary = "Login an existing user")
    public String loginAccount(
            @RequestParam @NotBlank(message = "Username is required") String username,
            @RequestParam @NotBlank(message = "Password is required") String password
    ){
            return authorizationService.loginAccount(username, password);
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout an existing user")
    public void logoutAccount(
            @RequestParam @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {

    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/password/{accountId}")
    @Operation(summary = "Update a users password")
    public void changePassword(
            @PathVariable @NotNull(message = "Driver ID cannot be blank") Long accountId,
            @RequestParam @NotNull(message = "Token cannot be blank") String token,
            @RequestParam @NotNull(message = "Old password required") String oldPassword,
            @RequestParam @NotNull(message = "New password required") String newPassword
    ){
        authorizationService.changePassword(accountId, token, oldPassword, newPassword);
    }


}
