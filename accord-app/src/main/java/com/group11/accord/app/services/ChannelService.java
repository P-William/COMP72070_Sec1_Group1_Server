package com.group11.accord.app.services;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.api.message.Message;
import com.group11.accord.api.message.NewImageMessage;
import com.group11.accord.api.message.NewTextMessage;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {

    public Channel createServerChannel(Long serverId, String channelName, Long accountId, String token) {
        return null;
    }

    public Channel createDmChannel(Long friendId, Long accountId, String token) {
        return null;
    }

    public List<Channel> getChannels(Long accountId, String token) {
        return null;
    }

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
