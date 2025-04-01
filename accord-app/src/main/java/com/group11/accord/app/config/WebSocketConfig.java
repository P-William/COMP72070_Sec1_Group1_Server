package com.group11.accord.app.config;

import com.group11.accord.app.websockets.auth.WebsocketAuthInterceptor;
import com.group11.accord.app.websockets.auth.WebsocketOutboundMessageInterceptor;
import com.group11.accord.app.websockets.auth.WebsocketSubscriptionInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebsocketAuthInterceptor websocketAuthInterceptor;
    private final WebsocketSubscriptionInterceptor websocketSubscriptionInterceptor;
    private final WebsocketOutboundMessageInterceptor websocketOutboundMessageInterceptor;

    private static final List<String> TOPICS = List.of(
        "/server",
        "/channel",
        "/user",
        "/message"
    );

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker(TOPICS.toArray(new String[0]));
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOrigins("*")
            .addInterceptors(websocketAuthInterceptor);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(websocketSubscriptionInterceptor);
    }

    public void configureClientOutboundChannel(ChannelRegistration registration) {
        registration.interceptors(websocketOutboundMessageInterceptor);
    }
}
