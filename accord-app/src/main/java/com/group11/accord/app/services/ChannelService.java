package com.group11.accord.app.services;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.api.message.Message;
import com.group11.accord.api.message.NewImageMessage;
import com.group11.accord.api.message.NewTextMessage;
import com.group11.accord.jpa.server.ServerJpa;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {
    private AuthorizationService authorizationService;
    private ServerService serverService;

    public Channel createServerChannel(Long serverId, String channelName, Long accountId, String token) {
        authorizationService.validateSession(accountId, token);

        ServerJpa server = serverService.findServerWithId(serverId);


        return null;
    }

    public List<Channel> getDmChannels(Long accountId, String token) {
        return null;
    }

    public List<Channel> getServerChannels(Long serverId, Long accountId, String token) { return null; }

    public List<Message> getChannelMessages(Long channelId, Long accountId, String token) {
        return null;
    }

    public void changeChannelName(Long channelId, String newName, Long accountId, String token) {

    }

    public void deleteChannel(Long channelId, Long accountId, String token) {

    }

    public void sendTextMessage(Long channelId, NewTextMessage newMessage, Long accountId, String token) {

    }

    public void sendImageMessage(Long channelId, NewImageMessage newMessage, Long accountId, String token) {

    }
}
