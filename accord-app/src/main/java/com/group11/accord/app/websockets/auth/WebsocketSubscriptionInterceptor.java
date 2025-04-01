package com.group11.accord.app.websockets.auth;

import com.group11.accord.app.services.AuthorizationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebsocketSubscriptionInterceptor implements ChannelInterceptor {

    private final SubscriptionRegistry subscriptionRegistry;
    private final AuthorizationService authorizationService;

    @Override
    public Message<?> preSend(@NonNull Message<?> message, @NonNull MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        StompCommand command = accessor.getCommand();

        if (accessor.getCommand() == null) {
            return message;
        }
        log.info("Received command {}", command);
        log.info("Received destination {}", accessor.getDestination());

        if (!StompCommand.SUBSCRIBE.equals(command)) {
            return message;
        }

        return handleSubscription(message, accessor);
    }

    private Message<?> handleSubscription(Message<?> message, StompHeaderAccessor accessor) {
        String destination = accessor.getDestination();

        if (destination == null) {
            log.warn("Websocket subscription unauthorized due to missing destination");
            return null;
        }

        String accountId = accessor.getFirstNativeHeader("accountId");
        String token = accessor.getFirstNativeHeader("token");


        if (accountId == null || token == null) {
            log.warn("Websocket subscription unauthorized due to missing accountId or token");
            return null;
        }

        boolean isValid = authorizationService.canSubscribe(Long.valueOf(accountId), token, destination);

        if (isValid) {
            log.info("Websocket subscription authorized for account {}", accountId);
            log.info(message.toString());

            String sessionId = accessor.getSessionId();
            String topic = accessor.getDestination();
            subscriptionRegistry.addSubscription(sessionId, Long.valueOf(accountId), topic);

            return message;
        } else {
            log.warn("Websocket subscription unauthorized for account {}", accountId);
            return null;
        }
    }
}
