package com.group11.accord.app.websockets.auth;

import com.group11.accord.app.services.AuthorizationService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebsocketAuthInterceptor implements HandshakeInterceptor {

    private final AuthorizationService authorizationService;

    @Override
    public boolean beforeHandshake(
        ServerHttpRequest request,
        @NonNull ServerHttpResponse response,
        @NonNull WebSocketHandler wsHandler,
        @NonNull Map<String, Object> attributes
    ) throws Exception {
        String query = request.getURI().getQuery();
        Map<String, String> queryParams = UriComponentsBuilder.fromUriString("?" + query).build()
            .getQueryParams()
            .toSingleValueMap();

        String accountId = queryParams.get("accountId");
        String token = queryParams.get("token");

        if (accountId == null || token == null) {
            log.debug("Websocket connection unauthorized due to missing accountId or token");
            return false;
        }

        boolean isValid = authorizationService.validateAuthToken(Long.valueOf(accountId), token);
        if (isValid) {
            log.info("Websocket connection authorized for account {}", accountId);
        } else {
            log.warn("Websocket connection unauthorized for account {}", accountId);
        }

        return isValid;
    }

    @Override
    public void afterHandshake(
        @NonNull ServerHttpRequest request,
        @NonNull ServerHttpResponse response,
        @NonNull WebSocketHandler wsHandler,
        Exception exception
    ) {
        // Do nothing
    }
}
