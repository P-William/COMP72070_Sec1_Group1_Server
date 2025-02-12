package com.group11.accord.app.services;

import com.group11.accord.api.server.members.ServerInvite;
import com.group11.accord.api.user.Account;
import com.group11.accord.api.user.friend.FriendRequest;
import com.group11.accord.app.exceptions.ErrorMessages;
import com.group11.accord.jpa.server.member.ServerInviteJpa;
import com.group11.accord.jpa.server.member.ServerInviteRepository;
import com.group11.accord.jpa.server.member.ServerMemberJpa;
import com.group11.accord.jpa.server.member.ServerMemberRepository;
import com.group11.accord.jpa.user.AccountJpa;
import com.group11.accord.jpa.user.AccountRepository;
import com.group11.accord.jpa.user.friend.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {
    private final ServerMemberRepository serverMemberRepository;
    private AccountRepository accountRepository;
    private AuthorizationService authorizationService;
    private FriendRequestRepository friendRequestRepository;
    private FriendRepository friendRepository;
    private ServerInviteRepository serverInviteRepository;

    public AccountService(ServerMemberRepository serverMemberRepository) {
        this.serverMemberRepository = serverMemberRepository;
    }

    public void updateUsername(Long id, String token, String username) {
        authorizationService.validateSession(id, token);

        AccountJpa accountJpa = accountRepository.findByUsername(username)
                //If there is no account without the given username we respond with an error
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_USERNAME.formatted(username)));

        if (accountRepository.existsByUsername(username)){
            throw new EntityExistsException("An account with that username already exists");
        }

        accountJpa.setUsername(username);
        accountRepository.save(accountJpa);
    }

    public Account getAccount(Long id, String token) {
        authorizationService.validateSession(id, token);

        AccountJpa accountJpa = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

        return accountJpa.toDto();
    }

    public List<Account> getFriends(Long id, String token) {
        authorizationService.validateSession(id, token);

        AccountJpa accountJpa = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

        return accountJpa.getFriends()
                .stream()
                .map(AccountJpa::toDto)
                .toList();
    }

    public List<FriendRequest> getFriendRequests(Long id, String token) {
        authorizationService.validateSession(id, token);

        AccountJpa accountJpa = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

        return friendRequestRepository
                .findAllByReceiver(accountJpa)
                .stream()
                .map(FriendRequestJpa::toDto)
                .toList();
    }

    public void sendFriendRequest(Long id, String token, String username) {
        authorizationService.validateSession(id, token);

        AccountJpa sender = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

        //Find receiver
        AccountJpa receiver = accountRepository.findByUsername(username)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_USERNAME.formatted(username)));

        //In case client side accidentally sends a friend request to an existing user
        if (!accountRepository.existsByFriends(receiver)){
            throw new EntityExistsException(ErrorMessages.ALREADY_FRIENDS.formatted(receiver.getUsername()));
        }

        //Check if a friend request to this user has already been sent by the sender
        if (!friendRequestRepository.existsBySenderAndReceiver(sender, receiver)) {
            throw new EntityExistsException(ErrorMessages.FRIEND_REQUEST_ALREADY_EXISTS.formatted(receiver.getUsername()));
        }

        friendRequestRepository.save(FriendRequestJpa.create(sender, receiver));
    }

    public void acceptFriendRequest(Long id, String token, Long requestId) {
        authorizationService.validateSession(id, token);

        AccountJpa sender = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

        FriendRequestJpa friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_FRIEND_REQUEST_WITH_ID.formatted(requestId)));

        AccountJpa friend = accountRepository.findById(friendRequest.getReceiver().getId())
                        .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

        //If by some means the client side trys to accept an existing request with an existing friend then we will deny it
        if (friendRepository.existsById(new FriendId(sender, friend))){
            throw new EntityExistsException(ErrorMessages.ALREADY_FRIENDS.formatted(friend.getUsername()));
        }

        friendRepository.save(FriendJpa.create(sender, friend));
    }

    public List<ServerInvite> getServerInvites(Long id, String token) {
        authorizationService.validateSession(id, token);

        AccountJpa accountJpa = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

        return serverInviteRepository
                .findAllByReceiver(accountJpa)
                .stream()
                .map(ServerInviteJpa::toDto)
                .toList();
    }

    public void acceptServerInvite(Long id, Long inviteId, String token) {
        authorizationService.validateSession(id, token);

        AccountJpa accountJpa = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

        ServerInviteJpa inviteJpa = serverInviteRepository.findById(inviteId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_SERVER_INVITE_WITH_ID.formatted(inviteId)));

        if (!serverMemberRepository.existsByIdAccount(accountJpa)){
            throw new EntityExistsException(ErrorMessages.ALREADY_MEMBER.formatted(inviteJpa.getServer().getName()));
        }

        serverMemberRepository.save(ServerMemberJpa.create(accountJpa, inviteJpa.getServer()));
    }
}