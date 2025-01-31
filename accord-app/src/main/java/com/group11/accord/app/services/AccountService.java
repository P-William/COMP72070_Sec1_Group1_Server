package com.group11.accord.app.services;

import com.group11.accord.api.server.members.ServerInvite;
import com.group11.accord.api.user.Account;
import com.group11.accord.api.user.friend.FriendRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountService {

    public void updateUsername(Long id, String token, String username) {

    }

    public Account getAccount(Long id, String token) {
        return null;
    }

    public List<Account> getFriends(Long id, String token) {
        return null;
    }

    public List<FriendRequest> getFriendRequests(Long id, String token) {
        return null;
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