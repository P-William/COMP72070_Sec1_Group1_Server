package com.group11.accord.app.websockets;

import com.group11.accord.api.RouteBuilder.ChannelRouteBuilder;
import com.group11.accord.api.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelPublisher {

    private final SimpMessagingTemplate messagingTemplate;

    public void publishChannel(Channel channel) {
        String topic = ChannelRouteBuilder.createdChannel(channel.serverId());
        messagingTemplate.convertAndSend(topic, channel);
    }

    public void publishChannelEdit(Channel channel) {
        String topic = ChannelRouteBuilder.editedChannel(channel.serverId());
        messagingTemplate.convertAndSend(topic, channel);
    }

    public void publishChannelDelete(Channel channel) {
        String topic = ChannelRouteBuilder.deletedChannel(channel.serverId());
        messagingTemplate.convertAndSend(topic, channel);
    }
}
