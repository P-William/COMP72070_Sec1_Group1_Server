package com.group11.accord.app.websockets.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SubscriptionRegistry {

    private final Map<String, String> sessionToTopicMap = new ConcurrentHashMap<>();
    private final Map<String, String> userToSessionMap = new ConcurrentHashMap<>();

    public void addSubscription(String sessionId, Long userId, String topic) {
        log.info("Adding subscription for session {} and user {} to topic {}", sessionId, userId, topic);
        sessionToTopicMap.put(sessionId, topic);
        userToSessionMap.put(userId.toString(), sessionId);
    }

    public void removeAllSubscriptionsBySession(String sessionId) {
        sessionToTopicMap.remove(sessionId);

        // Remove any user associated with this session
        userToSessionMap.values().removeIf(value -> value.equals(sessionId));
        log.info("Removing subscriptions for session {}", sessionId);
    }

    public void removeAllSubscriptionByUser(Long userId) {
        String sessionId = userToSessionMap.get(userId.toString());
        sessionToTopicMap.remove(sessionId);
        userToSessionMap.remove(userId.toString());
    }

    public void removeTopicSubscriptionBySession(String sessionId) {
        sessionToTopicMap.remove(sessionId);
    }

    public void removeTopicSubscriptionByUser(Long userId) {
        String sessionId = userToSessionMap.get(userId.toString());
        sessionToTopicMap.remove(sessionId);
    }

    public String getTopicBySession(String sessionId) {
        return sessionToTopicMap.get(sessionId);
    }

    public boolean sessionAuthorizedForTopic(String sessionId, String topic) {
        return topic.equals(sessionToTopicMap.get(sessionId));
    }
}
