package com.group11.accord.app.websockets;

import com.group11.accord.api.RouteBuilder.ServerRouteBuilder;
import com.group11.accord.api.server.ServerDeletion;
import com.group11.accord.api.server.ServerEdit;
import com.group11.accord.api.server.members.ServerBan;
import com.group11.accord.api.server.members.ServerKick;
import com.group11.accord.api.server.members.UserAdded;
import com.group11.accord.api.server.members.UserLeft;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerPublisher {

    private final SimpMessagingTemplate messagingTemplate;


    public void publishServerEdit(ServerEdit serverEdit) {
        String topic = ServerRouteBuilder.editServer(serverEdit);
        messagingTemplate.convertAndSend(topic, serverEdit);
    }

    public void publishServerDelete(ServerDeletion serverDeletion) {
        String topic = ServerRouteBuilder.deleteServer(serverDeletion);
        messagingTemplate.convertAndSend(topic, serverDeletion);
    }

    public void publishUserAdded(UserAdded userAdded) {
        String topic = ServerRouteBuilder.userAdded(userAdded);
        messagingTemplate.convertAndSend(topic, userAdded);
    }

    public void publishUserLeft(UserLeft userLeft) {
        String topic = ServerRouteBuilder.userRemoved(userLeft);
        messagingTemplate.convertAndSend(topic, userLeft);
    }

    public void publishServerKick(ServerKick serverKick) {
        String topic = ServerRouteBuilder.kickUser(serverKick);
        messagingTemplate.convertAndSend(topic, serverKick);
    }

    public void publishServerBan(ServerBan serverBan) {
        String topic = ServerRouteBuilder.banUser(serverBan);
        messagingTemplate.convertAndSend(topic, serverBan);
    }

}
