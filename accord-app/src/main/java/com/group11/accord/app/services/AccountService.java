package com.group11.accord.app.services;

import com.group11.accord.api.server.members.ServerInvite;
import com.group11.accord.api.user.Account;
import com.group11.accord.api.user.friend.FriendRequest;
import com.group11.accord.app.exceptions.ErrorMessages;
import com.group11.accord.jpa.channel.ChannelJpa;
import com.group11.accord.jpa.channel.ChannelRepository;
import com.group11.accord.jpa.channel.UserChannelJpa;
import com.group11.accord.jpa.channel.UserChannelRepository;
import com.group11.accord.jpa.file.FileJpa;
import com.group11.accord.jpa.server.member.ServerInviteJpa;
import com.group11.accord.jpa.server.member.ServerInviteRepository;
import com.group11.accord.jpa.server.member.ServerMemberJpa;
import com.group11.accord.jpa.server.member.ServerMemberRepository;
import com.group11.accord.jpa.user.AccountJpa;
import com.group11.accord.jpa.user.AccountRepository;
import com.group11.accord.jpa.user.friend.*;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final ServerMemberRepository serverMemberRepository;
    private final AccountRepository accountRepository;
    private final AuthorizationService authorizationService;
    private final FriendRequestRepository friendRequestRepository;
    private final FriendRepository friendRepository;
    private final ServerInviteRepository serverInviteRepository;
    private final FileService fileService;
    private final ChannelRepository channelRepository;
    private final UserChannelRepository userChannelRepository;

    public void updateUsername(Long id, String token, String username) {
        authorizationService.validateSession(id, token);

        AccountJpa accountJpa = accountRepository.findById(id)
                //If there is no account without the given username we respond with an error
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

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
        if (friendRepository.existsById(new FriendId(sender, receiver)) || friendRepository.existsById(new FriendId(receiver, sender))){
            throw new EntityExistsException(ErrorMessages.ALREADY_FRIENDS.formatted(receiver.getUsername()));
        }

        //Check if a friend request to this user has already been sent by the sender
        if (friendRequestRepository.existsBySenderAndReceiver(sender, receiver)) {
            throw new EntityExistsException(ErrorMessages.FRIEND_REQUEST_ALREADY_EXISTS.formatted(receiver.getUsername()));
        }

        friendRequestRepository.save(FriendRequestJpa.create(sender, receiver));
    }

    public void acceptFriendRequest(Long id, String token, Long requestId) {
        authorizationService.validateSession(id, token);

        AccountJpa friend = accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

        FriendRequestJpa friendRequest = friendRequestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_FRIEND_REQUEST_WITH_ID.formatted(requestId)));

        AccountJpa sender = accountRepository.findById(friendRequest.getSender().getId())
                        .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(id)));

        //If by some means the client side trys to accept an existing request with an existing friend then we will deny it
        if (friendRepository.existsById(new FriendId(sender, friend))){
            throw new EntityExistsException(ErrorMessages.ALREADY_FRIENDS.formatted(friend.getUsername()));
        }

        friendRepository.save(FriendJpa.create(sender, friend));
        friendRepository.save(FriendJpa.create(friend, sender));

        ChannelJpa channelJpa = ChannelJpa.create(sender.getUsername() + " and " + friend.getUsername(), true);
        channelRepository.save(channelJpa);

        userChannelRepository.save(UserChannelJpa.create(sender, friend, channelJpa));
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

        if (serverMemberRepository.existsByIdAccountIdAndIdServerId(accountJpa.getId(), inviteJpa.getServer().getId())){
            throw new EntityExistsException(ErrorMessages.ALREADY_MEMBER.formatted(inviteJpa.getServer().getName()));
        }

        serverMemberRepository.save(ServerMemberJpa.create(accountJpa, inviteJpa.getServer()));
    }

    public String changeProfilePic(Long accountId, String token, MultipartFile image){
        AccountJpa accountJpa = authorizationService.findValidAccount(accountId, token);

        accountJpa.setProfilePicUrl(fileService.saveImage(image));

        return accountJpa.getProfilePicUrl();
    }

    public AccountJpa findAccountWithId(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(accountId)));
    }

    public AccountJpa findAccountWithUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_USERNAME.formatted(username)));
    }

}