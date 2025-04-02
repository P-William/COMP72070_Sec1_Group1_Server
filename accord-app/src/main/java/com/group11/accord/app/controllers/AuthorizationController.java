package com.group11.accord.app.controllers;

import com.group11.accord.api.errors.returns.MinimalProblemDetail;
import com.group11.accord.api.errors.returns.MinimalValidationDetail;
import com.group11.accord.api.user.Account;
import com.group11.accord.api.user.NewAccount;
import com.group11.accord.api.user.SessionCredentials;
import com.group11.accord.app.services.AuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final AuthorizationService authorizationService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    @Operation(summary = "Create a new user account")
    @ApiResponse(responseCode = "201", description = "Account created successfully")
    @ApiResponse(responseCode = "400", description = "Invalid fields provided", content = {@Content(schema = @Schema(implementation = MinimalValidationDetail.class))})
    @ApiResponse(responseCode = "409", description = "Username already exists", content = {@Content(schema = @Schema(implementation = MinimalProblemDetail.class))})
    Account createAccount(@RequestBody @NotNull @Valid NewAccount newAccount){
        return authorizationService.createAccount(newAccount);
    }

    @PostMapping("/login")
    @Operation(summary = "Login an existing user")
    @ApiResponse(responseCode = "200", description = "Account logged in successfully, Note: Account id and token will be required for all requests that require authentication. Make sure to save it until the account is logged out.")
    @ApiResponse(responseCode = "400", description = "Invalid fields provided", content = {@Content(schema = @Schema(implementation = MinimalValidationDetail.class))})
    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = {@Content(schema = @Schema(implementation = MinimalProblemDetail.class))})
    @ApiResponse(responseCode = "404", description = "Account not found", content = {@Content(schema = @Schema(implementation = MinimalProblemDetail.class))})
    SessionCredentials loginAccount(
            @RequestParam @NotBlank(message = "Username is required") String username,
            @RequestParam @NotBlank(message = "Password is required") String password
    ){
            return authorizationService.loginAccount(username, password);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/logout")
    @Operation(summary = "Logout an existing user")
    @ApiResponse(responseCode = "201", description = "Logout was successful")
    @ApiResponse(responseCode = "400", description = "Invalid fields provided", content = {@Content(schema = @Schema(implementation = MinimalValidationDetail.class))})
    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = {@Content(schema = @Schema(implementation = MinimalProblemDetail.class))})
    @ApiResponse(responseCode = "404", description = "Account not found", content = {@Content(schema = @Schema(implementation = MinimalProblemDetail.class))})
    void logoutAccount(
            @RequestParam @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        authorizationService.logoutAccount(accountId, token);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/password/{accountId}")
    @Operation(summary = "Update a users password")
    @ApiResponse(responseCode = "200", description = "Password changed successfully")
    @ApiResponse(responseCode = "400", description = "Invalid fields provided", content = {@Content(schema = @Schema(implementation = MinimalValidationDetail.class))})
    @ApiResponse(responseCode = "401", description = "Invalid credentials", content = {@Content(schema = @Schema(implementation = MinimalProblemDetail.class))})
    @ApiResponse(responseCode = "404", description = "Account not found", content = {@Content(schema = @Schema(implementation = MinimalProblemDetail.class))})
    void changePassword(
            @PathVariable @NotNull(message = "Account ID cannot be blank") Long accountId,
            @RequestParam @NotNull(message = "Token cannot be blank") String token,
            @RequestParam @NotNull(message = "Old password required") String oldPassword,
            @RequestParam @NotNull(message = "New password required") String newPassword
    ){
        authorizationService.changePassword(accountId, token, oldPassword, newPassword);
    }


}
