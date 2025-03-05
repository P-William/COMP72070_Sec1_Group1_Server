package com.group11.accord.app.websockets;

import com.group11.accord.api.RouteBuilder.MessageRouteBuilder;
import com.group11.accord.api.message.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessagePublisher {

    private final SimpMessagingTemplate messagingTemplate;

    //TODO fix needed as Channel dto currently does not have the serverId
//    public void publishMessage(Message message) {
//        String topic = MessageRouteBuilder.newMessage(message.id(), message.channel().id());
//        messagingTemplate.convertAndSend(topic, message);
//    }
//
//    public void publishMessageEdit(Message message) {
//        String topic = MessageRouteBuilder.editedMessage(message.id(), message.channel().id());
//        messagingTemplate.convertAndSend(topic, message);
//    }
//
//    public void publishMessageDelete(Message message) {
//        String topic = MessageRouteBuilder.deletedMessage(message.id(), message.channel().id());
//        messagingTemplate.convertAndSend(topic, message);
//    }

}
