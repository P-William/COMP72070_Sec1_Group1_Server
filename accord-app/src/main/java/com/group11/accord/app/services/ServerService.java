package com.group11.accord.app.services;

import com.group11.accord.api.server.Server;
import com.group11.accord.api.server.members.NewServerBan;
import com.group11.accord.api.server.members.NewServerKick;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServerService {

    public void createServer(Long accountId, String token, String serverName) {

    }

    public List<Server> getServers(Long accountId, String token) {
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

}
