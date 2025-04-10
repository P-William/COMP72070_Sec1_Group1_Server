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
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

//TODO-Connor add annotations for swagger responses once controllers are done

@Slf4j
@Validated
@RestController
@RequestMapping("/account")
@Tag(name = "Account Controller", description = "Handles all operations regarding a users Account")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PatchMapping("/{accountId}/username")
    @Operation(summary = "Update a users username")
    void changeUsername(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "A new username is required") String username
    ){
        log.debug("Received request to change username from {}", accountId);
        accountService.updateUsername(accountId, token, username);
    }

    @GetMapping("/{accountId}")
    @Operation(summary = "Retrieve the account for a user")
    Account getAccount(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        log.debug("Received request for account {}", accountId);
        return accountService.getAccount(accountId, token);
    }

    @GetMapping("/{accountId}/friend")
    @Operation(summary = "Get a list of all a users friends")
    List<Account> getFriends(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        log.debug("Received request for friends list from {}", accountId);
        return accountService.getFriends(accountId, token);
    }

    @GetMapping("/{accountId}/friend/request")
    @Operation(summary = "Get a list of all friend requests")
    List<FriendRequest> getFriendRequests(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        log.debug("Received request from {} for a list of friend requests.", accountId);
        return accountService.getFriendRequests(accountId, token);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/{accountId}/friend/request")
    @Operation(summary = "Send a friend request to a specific user via their username")
    void sendFriendRequest(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "A valid username to send a friend request to is required") String username
    ) {
        log.debug("Received request to sent friend request from {} to user {}", accountId, username);
        accountService.sendFriendRequest(accountId, token, username);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{accountId}/friend/request/{requestId}")
    @Operation(summary = "Accept a friend request")
    void acceptFriendRequest(
            @PathVariable @NotNull(message = "The id of the FriendRequest is required") Long requestId,
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ){
        log.debug("Received request from {} to accept friend request {} as friend.", accountId, requestId);
        accountService.acceptFriendRequest(accountId, token, requestId);
    }

    @GetMapping("/{accountId}/server/invite")
    @Operation(summary = "Get a list of all server invites")
    List<ServerInvite> getServerInvites(
        @PathVariable @NotNull(message = "Account ID is required") Long accountId,
        @RequestParam @NotNull(message = "Token is required") String token
    ){
        log.debug("Received request from {} to get a list of server invites.", accountId);
        return accountService.getServerInvites(accountId, token);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping("/{accountId}/server/invite/{inviteId}")
    @Operation(summary = "Accept an invite to a server")
    void acceptServerInvite(
            @PathVariable @NotNull(message = "The ID of the ServerInvite is required") Long inviteId,
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token
    ) {
        log.debug("Received request from {} to accept server invite {}", accountId, inviteId);
        accountService.acceptServerInvite(accountId, inviteId, token);
    }

    @PatchMapping(value = "/{accountId}/pic", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update a users profile picture")
    String changeProfilePicture(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "A valid image is required") MultipartFile image
    ) {
        log.debug("Received request from {} to update profile picture.", accountId);
        return accountService.changeProfilePic(accountId, token, image);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PatchMapping(value = "/{accountId}/bio")
    @Operation(summary = "Update a users bio")
    void changeBio(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestBody @NotNull(message = "A new user biography is required") String newBio
    ) {
        
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping(value = "/{accountId}/friend")
    @Operation(summary = "Remove a friend")
    void removeFriend(
            @PathVariable @NotNull(message = "Account ID is required") Long accountId,
            @RequestParam @NotNull(message = "Token is required") String token,
            @RequestParam @NotNull(message = "A id of a friend is needed") Long friendId
    ) {
        accountService.removeFriend(accountId, token, friendId);
    }

}
