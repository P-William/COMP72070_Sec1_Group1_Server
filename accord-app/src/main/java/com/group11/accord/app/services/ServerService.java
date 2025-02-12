package com.group11.accord.app.services;

import com.group11.accord.api.server.BasicServer;
import com.group11.accord.api.server.members.NewServerBan;
import com.group11.accord.api.server.members.NewServerKick;
import com.group11.accord.app.exceptions.ErrorMessages;
import com.group11.accord.app.exceptions.InvalidCredentialsException;
import com.group11.accord.jpa.server.ServerJpa;
import com.group11.accord.jpa.server.ServerRepository;
import com.group11.accord.jpa.user.AccountJpa;
import com.group11.accord.jpa.user.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.security.auth.message.AuthException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {
    private AuthorizationService authorizationService;
    private ServerRepository serverRepository;
    private AccountRepository accountRepository;

    public void createServer(Long accountId, String token, String serverName) {
        authorizationService.validateSession(accountId, token);

        AccountJpa accountJpa = accountRepository.findById(accountId)
                        .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(accountId)));

        serverRepository.save(ServerJpa.create(serverName, accountJpa));
    }

    public List<BasicServer> getServers(Long accountId, String token) {
        return null;
    }

    public void changeServerName(Long serverId, Long accountId, String token, String newName) {

    }

    public void deleteServer(Long serverId, Long accountId, String token) {

    }

    public void kickFromServer(Long serverId, Long accountId, String token, NewServerKick kickUpload) {

    }

    public void banFromServer(Long serverId, Long accountId, String token, NewServerBan banUpload) {

    }

    public void leaveServer(Long serverId, Long accountId, String token) {

    }

    public void validateOwner(Long serverId, Long accountId, String token){
        /*Undecided if this should be here or separate since
        * we don't validate if someone is the owner if we don't
        * validate they are logged in */
        authorizationService.validateSession(accountId, token);
    }

}
