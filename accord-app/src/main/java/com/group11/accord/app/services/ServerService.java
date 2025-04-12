package com.group11.accord.app.services;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.api.server.BasicServer;
import com.group11.accord.api.server.ServerDeletion;
import com.group11.accord.api.server.ServerEdit;
import com.group11.accord.api.server.members.NewServerBan;
import com.group11.accord.api.server.members.NewServerKick;
import com.group11.accord.api.user.Account;
import com.group11.accord.app.exceptions.AccountNotAuthorizedException;
import com.group11.accord.app.exceptions.ErrorMessages;
import com.group11.accord.app.exceptions.InvalidOperationException;
import com.group11.accord.app.exceptions.ServerErrorException;
import com.group11.accord.app.websockets.ChannelPublisher;
import com.group11.accord.app.websockets.ServerPublisher;
import com.group11.accord.app.websockets.UserPublisher;
import com.group11.accord.jpa.channel.ChannelJpa;
import com.group11.accord.jpa.channel.ChannelRepository;
import com.group11.accord.jpa.channel.ServerChannelJpa;
import com.group11.accord.jpa.channel.ServerChannelRepository;
import com.group11.accord.jpa.server.ServerJpa;
import com.group11.accord.jpa.server.ServerRepository;
import com.group11.accord.jpa.server.member.*;
import com.group11.accord.jpa.user.AccountJpa;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ServerService {
    private final ServerKickRepository serverKickRepository;
    private final AccountService accountService;
    private final ServerMemberRepository serverMemberRepository;
    private final ServerBanRepository serverBanRepository;
    private final AuthorizationService authorizationService;
    private final ServerRepository serverRepository;
    private final ServerInviteRepository serverInviteRepository;

    private final static String defaultChannel = "General";
    private final ChannelRepository channelRepository;
    private final ServerChannelRepository serverChannelRepository;
    private final UserPublisher userPublisher;
    private final ServerPublisher serverPublisher;
    private final ChannelPublisher channelPublisher;

    public Channel createServerChannel(Long serverId, String channelName, Long accountId, String token) {
        authorizationService.validateSession(accountId, token);

        ServerJpa server = validateOwner(serverId, accountId, token);
        ChannelJpa channelJpa = ChannelJpa.create(channelName, false);

        channelRepository.save(channelJpa);

        ServerChannelJpa serverChannelJpa = serverChannelRepository.save(ServerChannelJpa.create(server, channelJpa));
        channelPublisher.publishChannel(serverChannelJpa.getId().getServer().getId(), serverChannelJpa.toDto());
        return serverChannelJpa.toDto();
    }

    public BasicServer createServer(Long accountId, String token, String serverName) {
        AccountJpa accountJpa = authorizationService.findValidAccount(accountId, token);

        ServerJpa serverJpa = ServerJpa.create(serverName, accountJpa);

        ServerJpa server = serverRepository.save(serverJpa);

        addServerMember(accountJpa, serverJpa);

        createServerChannel(server.getId(), defaultChannel, accountId, token);

        return server.toBasicDto();
    }

    public List<BasicServer> getServers(Long accountId, String token) {
        AccountJpa accountJpa = authorizationService.findValidAccount(accountId, token);

        return serverRepository
                .findAllByMembersIsContaining(accountJpa)
                .stream()
                .map(ServerJpa::toBasicDto)
                .toList();
    }

    public void changeServerName(Long serverId, Long accountId, String token, String newName) {
        ServerJpa serverJpa = validateOwner(serverId, accountId, token);

        serverJpa.setName(newName);

        serverRepository.save(serverJpa);

        serverPublisher.publishServerEdit(new ServerEdit(serverJpa.getId(), serverJpa.getName(), serverJpa.getOwner().toDto()));
    }

    public void deleteServer(Long serverId, Long accountId, String token) {
        ServerJpa serverJpa = validateOwner(serverId, accountId, token);

        serverPublisher.publishServerDelete(new ServerDeletion(serverJpa.getId(), serverJpa.getName()));

        //Change to scheduled job probably
        serverRepository.delete(serverJpa);
    }

    public void inviteToServer(Long serverId, String username, Long accountId, String token) {
        ServerJpa serverJpa = validateOwner(serverId, accountId, token);
        verifyIsServerMember(accountId, serverId);

        if (serverBanRepository.existsByBannedUserUsernameAndServerId(username, serverId)) {
            throw new InvalidOperationException(ErrorMessages.USER_BANNED_FROM_SERVER.formatted(username));
        }

        boolean alreadyMember = serverJpa.getMembers()
                .stream()
                .anyMatch(member -> member.getUsername().equals(username));
        if (alreadyMember){
            throw new ServerErrorException(ErrorMessages.ALREADY_MEMBER.formatted(username));
        }

        AccountJpa sender = accountService.findAccountWithId(accountId);
        AccountJpa receiver = accountService.findAccountWithUsername(username);

        ServerInviteJpa serverInviteJpa = ServerInviteJpa.create(sender, receiver, serverJpa);
        serverInviteRepository.save(serverInviteJpa);
        userPublisher.publishNewServerInvite(receiver.getId(), serverInviteJpa.toDto());
    }

    public void kickFromServer(Long serverId, Long accountId, String token, @NotNull NewServerKick kickUpload) {
        ServerJpa server = validateOwner(serverId, accountId, token);

        AccountJpa kickedUser = accountService.findAccountWithId(kickUpload.kickedUserId());

        verifyIsServerMember(kickedUser.getId(), serverId);

        AccountJpa kickedBy = accountService.findAccountWithId(accountId);

        verifyIsServerMember(kickedBy.getId(), serverId);

        ServerKickJpa serverKickJpa = ServerKickJpa.create(server, kickedUser, kickedBy, kickUpload.reason());
        serverKickRepository.save(serverKickJpa);
        serverPublisher.publishServerKick(serverKickJpa.toDto());

        //Probably doesn't need to be a job since a user should be instantly banned
        serverMemberRepository.deleteByIdAccountIdAndIdServerId(kickedUser.getId(), serverId);
    }

    public void banFromServer(Long serverId, Long accountId, String token, @NotNull NewServerBan banUpload) {
        ServerJpa server = validateOwner(serverId, accountId, token);

        AccountJpa bannedUser = accountService.findAccountWithId(banUpload.bannedUserId());

        verifyIsServerMember(bannedUser.getId(), serverId);

        AccountJpa bannedBy = accountService.findAccountWithId(accountId);

        verifyIsServerMember(bannedBy.getId(), serverId);

        ServerBanJpa serverBanJpa = ServerBanJpa.create(server, bannedUser, bannedBy, banUpload.reason());
        serverBanRepository.save(serverBanJpa);
        serverPublisher.publishServerBan(serverBanJpa.toDto());

        //Probably doesn't need to be a job since a user should be instantly banned
        serverMemberRepository.deleteByIdAccountIdAndIdServerId(bannedUser.getId(), serverId);
    }

    public void leaveServer(Long serverId, Long accountId, String token) {
        authorizationService.validateSession(accountId, token);

        verifyIsServerMember(accountId, serverId);

        ServerJpa serverJpa = findServerWithId(serverId);
        if (serverJpa.getOwner().getId().equals(accountId)) {
            throw new InvalidOperationException(ErrorMessages.OWNER_CANNOT_LEAVE_SERVER);
        }

        serverMemberRepository.deleteByIdAccountIdAndIdServerId(accountId, serverId);
    }

    public List<Account> getServerMembers(Long serverId, Long accountId, String token) {
        AccountJpa accountJpa = authorizationService.findValidAccount(accountId, token);

        verifyIsServerMember(accountJpa.getId(), serverId);

        ServerJpa serverJpa = findServerWithId(serverId);

        return serverJpa
                .getMembers()
                .stream()
                .map(AccountJpa::toDto)
                .toList();
    }

    public void transferOwnership(Long serverId, Long newOwnerId, Long accountId, String token) {
        ServerJpa serverJpa = validateOwner(serverId, accountId, token);

        verifyIsServerMember(newOwnerId, serverId);

        AccountJpa newOwner = accountService.findAccountWithId(newOwnerId);

        serverJpa.setOwner(newOwner);

        serverJpa = serverRepository.save(serverJpa);

        serverPublisher.publishServerEdit(new ServerEdit(serverJpa.getId(), serverJpa.getName(), serverJpa.getOwner().toDto()));
    }

    public ServerJpa validateOwner(Long serverId, Long accountId, String token){
        authorizationService.validateSession(accountId, token);

        if (!serverRepository.existsByOwnerIdAndId(accountId, serverId)){
            throw new AccountNotAuthorizedException(ErrorMessages.INVALID_SERVER_AUTHORITY.formatted(accountId));
        }

        return serverRepository.findByOwnerIdAndId(accountId, serverId);
    }

    public ServerJpa findServerWithId(Long serverId) {
        return serverRepository.findById(serverId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_SERVER_WITH_ID.formatted(serverId)));
    }

    public void addServerMember(AccountJpa accountJpa, ServerJpa serverJpa) {
        serverMemberRepository.save(ServerMemberJpa.create(accountJpa, serverJpa));
    }

    public void verifyIsServerMember(Long accountId, Long serverId){
        if (!serverMemberRepository.existsByIdAccountIdAndIdServerId(accountId, serverId)) {
            throw new EntityNotFoundException(ErrorMessages.NOT_MEMBER.formatted(serverId));
        }
    }
}
