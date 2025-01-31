package com.group11.accord.app.controllers;

import com.group11.accord.api.server.members.ServerInvite;
import com.group11.accord.api.user.Account;
import com.group11.accord.api.user.friend.FriendRequest;
import com.group11.accord.app.services.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping("/account")
@Tag(name = "Account Controller", description = "Handles all operations regarding a users Account")
@RequiredArgsConstructor
public class AccountController {

    AccountService accountService;

    @PostMapping("/username/{accountId}")
    @Operation(description = "Update a users username")
    void changeUsername(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "A new username is required") String username
    ){
        accountService.updateUsername(accountId, token, username);
    }

    @GetMapping("/{accountId}")
    @Operation(description = "Retrieve the account for a user")
    Account getAccount(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        return accountService.getAccount(accountId, token);
    }

    @GetMapping("/friend/{accountId}")
    @Operation(description = "Get a list of all a users friends")
    List<Account> getFriends(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        return accountService.getFriends(accountId, token);
    }

    @GetMapping("/friend/requests/{accountId}")
    @Operation(description = "Get a list of all friend requests")
    List<FriendRequest> getFriendRequests(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        return accountService.getFriendRequests(accountId, token);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/friend/request/{accountId}")
    @Operation(description = "Send a friend request to a specific user via their username")
    void sendFriendRequest(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "A valid username to send a friend request to is required") String username
    ) {
        accountService.sendFriendRequest(accountId, token, username);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/friend/request/{accountId}")
    @Operation(description = "Accept a friend request")
    void acceptFriendRequest(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "The id of the FriendRequest is required") Long requestId
    ){
        accountService.acceptFriendRequest(accountId, token, requestId);
    }

    @GetMapping("/server/invite/{accountId}")
    @Operation(description = "Get a list of all server invites")
    List<ServerInvite> getServerInvites(
        @PathVariable @NotNull(message = "Account ID is required") Long accountId,
        @RequestParam @NotNull(message = "Token is required") String token
    ){
        return accountService.getServerInvites(accountId, token);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/server/invite/{accountId}/{inviteId}")
    void acceptServerInvite(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @PathVariable @NotNull(message = "The ID of the ServerInvite is required") Long inviteId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        accountService.acceptServerInvite(accountId, inviteId, token);
    }

}
