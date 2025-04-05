package com.group11.accord.app.websockets;

import com.group11.accord.api.RouteBuilder.UserRouteBuilder;
import com.group11.accord.api.server.members.ServerInvite;
import com.group11.accord.api.user.Account;
import com.group11.accord.api.user.friend.FriendRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishFriendRequest(FriendRequest friendRequest) {
         String topic = UserRouteBuilder.friendRequest(friendRequest);
         messagingTemplate.convertAndSend(topic, friendRequest);
         log.debug("Publishing new friend requests to {}", friendRequest.recipient().id());
    }

    public void publishFriendAccept(Long userId, Account newFriend) {
        String topic = UserRouteBuilder.acceptFriendRequest(userId);
        messagingTemplate.convertAndSend(topic, newFriend);
        log.debug("Publishing friend request accepted to {} for new friend {}", userId, newFriend.id());
    }

    public void publishFriendDecline(Long userId, Account friend) {
        String topic = UserRouteBuilder.declineFriendRequest(userId);
        messagingTemplate.convertAndSend(topic, friend);
        log.debug("Publishing friend request declined to {} for {}", userId, friend.id());
    }

    public void publishFriendRemove(Long userId, Account friend) {
        String topic = UserRouteBuilder.removeFriend(userId);
        messagingTemplate.convertAndSend(topic, friend);
        log.debug("Publishing friend removed to {} for friend {}", userId, friend.id());
    }

    public void publishAccountEdit(Long userId, Account account) {
        String topic = UserRouteBuilder.accountEdit(userId);
        messagingTemplate.convertAndSend(topic, account);
        log.debug("Publishing account edit to {} for changed account {}", userId, account.id());
    }

    public void publishAccountDelete(Long userId, Account account) {
        String topic = UserRouteBuilder.accountDelete(userId);
        messagingTemplate.convertAndSend(topic, account);
        log.debug("Publishing account deleted to {} for account {}", userId, account.id());
    }

    public void publishNewServerInvite(Long userId, ServerInvite account) {
        String topic = UserRouteBuilder.newServerInvite(userId);
        messagingTemplate.convertAndSend(topic, account);
        log.debug("Publishing new server invite to {} for account {}", userId, account.id());
    }
}
