package com.group11.accord.app.websockets;

import com.group11.accord.api.RouteBuilder.ServerRouteBuilder;
import com.group11.accord.api.server.ServerDeletion;
import com.group11.accord.api.server.ServerEdit;
import com.group11.accord.api.server.members.ServerBan;
import com.group11.accord.api.server.members.ServerKick;
import com.group11.accord.api.server.members.UserAdded;
import com.group11.accord.api.server.members.UserLeft;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerPublisher {

    private final SimpMessagingTemplate messagingTemplate;


    public void publishServerEdit(ServerEdit serverEdit) {
        String topic = ServerRouteBuilder.editServer(serverEdit);
        messagingTemplate.convertAndSend(topic, serverEdit);
        log.debug("Publishing server edit for server {}", serverEdit.id());
    }

    public void publishServerDelete(ServerDeletion serverDeletion) {
        String topic = ServerRouteBuilder.deleteServer(serverDeletion);
        messagingTemplate.convertAndSend(topic, serverDeletion);
        log.debug("Publishing server delete for server {}", serverDeletion.id());
    }

    public void publishUserAdded(UserAdded userAdded) {
        String topic = ServerRouteBuilder.userAdded(userAdded);
        messagingTemplate.convertAndSend(topic, userAdded);
        log.debug("Publishing user added to server for server {} and new user {}", userAdded.server().id(), userAdded.account().id());
    }

    public void publishUserLeft(UserLeft userLeft) {
        String topic = ServerRouteBuilder.userRemoved(userLeft);
        messagingTemplate.convertAndSend(topic, userLeft);
        log.debug("Publishing user left server {} for user {}", userLeft.server().id(), userLeft.account().id());
    }

    public void publishServerKick(ServerKick serverKick) {
        String topic = ServerRouteBuilder.kickUser(serverKick);
        messagingTemplate.convertAndSend(topic, serverKick);
        log.debug("Publishing server kick from server {} for user {}", serverKick.server().id(), serverKick.kickedUser().id());
    }

    public void publishServerBan(ServerBan serverBan) {
        String topic = ServerRouteBuilder.banUser(serverBan);
        messagingTemplate.convertAndSend(topic, serverBan);
        log.debug("Publishing server ban from server {} for user {}", serverBan.server().id(), serverBan.bannedUser().id());
    }

}
