package com.group11.accord.app.services;

import com.group11.accord.api.server.BasicServer;
import com.group11.accord.api.server.Server;
import com.group11.accord.api.server.members.NewServerBan;
import com.group11.accord.api.server.members.NewServerKick;
import com.group11.accord.app.exceptions.AccountNotAuthorizedException;
import com.group11.accord.app.exceptions.ErrorMessages;
import com.group11.accord.app.exceptions.InvalidCredentialsException;
import com.group11.accord.app.exceptions.ServerErrorException;
import com.group11.accord.jpa.server.ServerJpa;
import com.group11.accord.jpa.server.ServerRepository;
import com.group11.accord.jpa.server.member.*;
import com.group11.accord.jpa.user.AccountJpa;
import com.group11.accord.jpa.user.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
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

    public BasicServer createServer(Long accountId, String token, String serverName) {
        AccountJpa accountJpa = authorizationService.findValidAccount(accountId, token);

        ServerJpa serverJpa = ServerJpa.create(serverName, accountJpa);

        ServerJpa server = serverRepository.save(serverJpa);

        addServerMember(accountJpa, serverJpa);

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
    }

    public void deleteServer(Long serverId, Long accountId, String token) {
        validateOwner(serverId, accountId, token);

        //Change to scheduled job probably
        serverRepository.deleteById(serverId);
    }

    public void inviteToServer(Long serverId, String username, Long accountId, String token) {
        ServerJpa serverJpa = validateOwner(serverId, accountId, token);
        verifyIsServerMember(accountId, serverId);

        boolean alreadyMember = serverJpa.getMembers()
                .stream()
                .anyMatch(member -> member.getUsername().equals(username));
        if (alreadyMember){
            throw new ServerErrorException(ErrorMessages.ALREADY_MEMBER.formatted(username));
        }

        AccountJpa sender = accountService.findAccountWithId(accountId);
        AccountJpa receiver = accountService.findAccountWithUsername(username);

        serverInviteRepository.save(ServerInviteJpa.create(sender, receiver, serverJpa));
    }

    public void kickFromServer(Long serverId, Long accountId, String token, NewServerKick kickUpload) {
        ServerJpa server = validateOwner(serverId, accountId, token);

        AccountJpa kickedUser = accountService.findAccountWithId(kickUpload.kickedUser().id());

        verifyIsServerMember(kickedUser.getId(), serverId);

        AccountJpa kickedBy = accountService.findAccountWithId(accountId);

        verifyIsServerMember(kickedBy.getId(), serverId);

        serverKickRepository.save(ServerKickJpa.create(server, kickedUser, kickedBy, kickUpload.reason()));

        //Probably doesn't need to be a job since a user should be instantly banned
        serverMemberRepository.deleteByIdAccountIdAndIdServerId(kickedUser.getId(), serverId);
    }

    public void banFromServer(Long serverId, Long accountId, String token, NewServerBan banUpload) {
        ServerJpa server = validateOwner(serverId, accountId, token);

        AccountJpa bannedUser = accountService.findAccountWithId(banUpload.bannedUser().id());

        verifyIsServerMember(bannedUser.getId(), serverId);

        AccountJpa bannedBy = accountService.findAccountWithId(accountId);

        verifyIsServerMember(bannedBy.getId(), serverId);

        serverBanRepository.save(ServerBanJpa.create(server, bannedUser, bannedBy, banUpload.reason()));

        //Probably doesn't need to be a job since a user should be instantly banned
        serverMemberRepository.deleteByIdAccountIdAndIdServerId(bannedUser.getId(), serverId);
    }

    public void leaveServer(Long serverId, Long accountId, String token) {
        authorizationService.validateSession(accountId, token);

        verifyIsServerMember(accountId, serverId);

        serverMemberRepository.deleteByIdAccountIdAndIdServerId(accountId, serverId);
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
