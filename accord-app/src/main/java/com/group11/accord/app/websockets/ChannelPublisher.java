package com.group11.accord.app.websockets;

import com.group11.accord.api.RouteBuilder.ChannelRouteBuilder;
import com.group11.accord.api.channel.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishChannel(Long serverId, Channel channel) {
        String topic = ChannelRouteBuilder.createdChannel(serverId);
        messagingTemplate.convertAndSend(topic, channel);
        log.debug("Publishing new channel created in server {}", serverId);
    }

    public void publishChannelEdit(Long serverId, Channel channel) {
        String topic = ChannelRouteBuilder.editedChannel(serverId);
        messagingTemplate.convertAndSend(topic, channel);
        log.debug("Publishing channel edit for channel {} in server {}", channel.id(), serverId);
    }

    public void publishChannelDelete(Long serverId, Channel channel) {
        String topic = ChannelRouteBuilder.deletedChannel(serverId);
        messagingTemplate.convertAndSend(topic, channel);
        log.debug("Publishing channel delete for channel {} in server {}", channel.id(), serverId);
    }
}
