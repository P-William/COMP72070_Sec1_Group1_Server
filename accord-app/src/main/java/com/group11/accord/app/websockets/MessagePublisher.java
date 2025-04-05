package com.group11.accord.app.websockets;

import com.group11.accord.api.RouteBuilder.MessageRouteBuilder;
import com.group11.accord.api.message.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagePublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishMessage(Message message) {
        String topic = MessageRouteBuilder.newMessage(message.id(), message.channelId());
        messagingTemplate.convertAndSend(topic, message);
        log.debug("Publishing new message {} to channel {} from account {}", message.id(), message.channelId(), message.account().id());
    }

    public void publishMessageEdit(Message message) {
        String topic = MessageRouteBuilder.editedMessage(message.id(), message.channelId());
        messagingTemplate.convertAndSend(topic, message);
        log.debug("Publishing edit message {} in channel {} from account {}", message.id(), message.channelId(), message.account().id());
    }

    public void publishMessageDelete(Message message) {
        String topic = MessageRouteBuilder.deletedMessage(message.id(), message.channelId());
        messagingTemplate.convertAndSend(topic, message);
        log.debug("Publishing delete message {} in channel {} from account {}", message.id(), message.channelId(), message.account().id());
    }

}
