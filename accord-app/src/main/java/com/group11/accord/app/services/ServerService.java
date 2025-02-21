package com.group11.accord.app.services;

import com.group11.accord.api.server.BasicServer;
import com.group11.accord.api.server.members.NewServerBan;
import com.group11.accord.api.server.members.NewServerKick;
import com.group11.accord.app.exceptions.AccountNotAuthorizedException;
import com.group11.accord.app.exceptions.ErrorMessages;
import com.group11.accord.app.exceptions.InvalidCredentialsException;
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
    private ServerKickRepository serverKickRepository;
    private AccountService accountService;
    private ServerMemberRepository serverMemberRepository;
    private ServerBanRepository serverBanRepository;
    private AuthorizationService authorizationService;
    private ServerRepository serverRepository;

    public void createServer(Long accountId, String token, String serverName) {
        AccountJpa accountJpa = authorizationService.findValidAccount(accountId, token);

        serverRepository.save(ServerJpa.create(serverName, accountJpa));
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

    public void kickFromServer(Long serverId, Long accountId, String token, NewServerKick kickUpload) {
        ServerJpa server = validateOwner(serverId, accountId, token);

        verifyIsServerMember(kickUpload.kickedUser().id(), serverId);

        AccountJpa kickedUser = accountService.findAccountWithId(kickUpload.kickedUser().id());

        AccountJpa kickedBy = accountService.findAccountWithId(kickUpload.kickedBy().id());

        serverKickRepository.save(ServerKickJpa.create(server, kickedUser, kickedBy, kickUpload.reason()));

        serverMemberRepository.deleteByIdAccountIdAndIdServerId(kickedUser.getId(), serverId);
    }

    public void banFromServer(Long serverId, Long accountId, String token, NewServerBan banUpload) {
        ServerJpa server = validateOwner(serverId, accountId, token);

        verifyIsServerMember(banUpload.bannedUser().id(), serverId);

        AccountJpa bannedUser = accountService.findAccountWithId(banUpload.bannedUser().id());

        AccountJpa bannedBy = accountService.findAccountWithId(banUpload.bannedBy().id());

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

    public void verifyIsServerMember(Long accountId, Long serverId){
        if (!serverMemberRepository.existsByIdAccountIdAndIdServerId(accountId, serverId)) {
            throw new EntityNotFoundException(ErrorMessages.NOT_MEMBER.formatted(serverId));
        }
    }
}
