package com.group11.accord.app.websockets.auth;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebsocketOutboundMessageInterceptor implements ChannelInterceptor {

    private final SubscriptionRegistry subscriptionRegistry;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        if (!isMessage(accessor)) {
            return message;
        }

        String sessionId = accessor.getSessionId();
        String topic = accessor.getDestination();

        if (topic == null) {
            log.warn("Websocket message unauthorized due to missing destination");
            return null;
        }

        if (!subscriptionRegistry.sessionAuthorizedForTopic(sessionId, topic)) {
            log.info("User is not authorized to receive messages for this topic. Skipping message.");
            return null;
        }

        return message;
    }

    private boolean isMessage(StompHeaderAccessor accessor) {
        MessageHeaders headers = accessor.getMessageHeaders();

        Object simpMessageType = headers.get("simpMessageType");
        if (simpMessageType == null) {
            return false;
        }
        if (!(simpMessageType instanceof SimpMessageType type)) {
            return false;
        }

        return type.name().equals("MESSAGE");
    }
}
