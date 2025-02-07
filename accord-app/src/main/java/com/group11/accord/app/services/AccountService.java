package com.group11.accord.app.services;

import com.group11.accord.api.server.members.ServerInvite;
import com.group11.accord.api.user.Account;
import com.group11.accord.api.user.friend.FriendRequest;
import com.group11.accord.app.exceptions.ErrorMessages;
import com.group11.accord.jpa.user.AccountJpa;
import com.group11.accord.jpa.user.AccountRepository;
import com.group11.accord.jpa.user.FriendRequestJpa;
import com.group11.accord.jpa.user.FriendRequestRepository;
import com.group11.accord.jpa.user.session.SessionRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    private AuthorizationService authorizationService;
    private FriendRequestRepository friendRequestRepository;

    public void updateUsername(Long id, String token, String username) {
        AccountJpa accountJpa = accountRepository.findByUsername(username)
                //If there is no account without the given username we respond with an error
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_USERNAME.formatted(username)));

        authorizationService.validateSession(id, token);

        if (accountRepository.existsByUsername(username)){
            throw new EntityExistsException("An account with that username already exists");
        }

        accountJpa.setUsername(username);
        accountRepository.save(accountJpa);
    }

    public Account getAccount(Long id, String token) {
        AccountJpa accountJpa = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

        authorizationService.validateSession(id, token);

        return accountJpa.toDto();
    }

    public List<Account> getFriends(Long id, String token) {
        AccountJpa accountJpa = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

        authorizationService.validateSession(id, token);

        List<Account> friends = new ArrayList<>();
        for (AccountJpa friend : accountJpa.getFriends()){
            friends.add(friend.toDto());
        }

        return friends;
    }

    public List<FriendRequest> getFriendRequests(Long id, String token) {
        AccountJpa accountJpa = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

        authorizationService.validateSession(id, token);

        return friendRequestRepository
                .findAllByReceiver(accountJpa)
                .stream()
                .map(FriendRequestJpa::toDto)
                .toList();
    }

    public void sendFriendRequest(Long id, String token, String username) {

    }

    public void acceptFriendRequest(Long id, String token, Long requestId) {

    }

    public List<ServerInvite> getServerInvites(Long id, String token) {
        return null;
    }

    public void acceptServerInvite(Long id, Long inviteId, String token) {}
}