package com.group11.accord.app.websockets;

import com.group11.accord.api.RouteBuilder.UserRouteBuilder;
import com.group11.accord.api.server.members.ServerInvite;
import com.group11.accord.api.user.Account;
import com.group11.accord.api.user.friend.FriendRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishFriendRequest(FriendRequest friendRequest) {
         String topic = UserRouteBuilder.friendRequest(friendRequest);
         messagingTemplate.convertAndSend(topic, friendRequest);
    }

    public void publishFriendAccept(Long userId, Account newFriend) {
        String topic = UserRouteBuilder.acceptFriendRequest(userId);
        messagingTemplate.convertAndSend(topic, newFriend);
    }

    public void publishFriendDecline(Long userId, Account friend) {
        String topic = UserRouteBuilder.declineFriendRequest(userId);
        messagingTemplate.convertAndSend(topic, friend);
    }

    public void publishFriendRemove(Long userId, Account friend) {
        String topic = UserRouteBuilder.removeFriend(userId);
        messagingTemplate.convertAndSend(topic, friend);
    }

    public void publishAccountEdit(Long userId, Account account) {
        String topic = UserRouteBuilder.accountEdit(userId);
        messagingTemplate.convertAndSend(topic, account);
    }

    public void publishAccountDelete(Long userId, Account account) {
        String topic = UserRouteBuilder.accountDelete(userId);
        messagingTemplate.convertAndSend(topic, account);
    }

    public void publishNewServerInvite(Long userId, ServerInvite account) {
        String topic = UserRouteBuilder.newServerInvite(userId);
        messagingTemplate.convertAndSend(topic, account);
    }



}
