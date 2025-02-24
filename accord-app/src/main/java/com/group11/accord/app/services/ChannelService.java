package com.group11.accord.app.services;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.api.message.Message;
import com.group11.accord.api.message.NewImageMessage;
import com.group11.accord.api.message.NewTextMessage;
import com.group11.accord.app.exceptions.ErrorMessages;
import com.group11.accord.jpa.channel.*;
import com.group11.accord.jpa.message.MessageJpa;
import com.group11.accord.jpa.server.ServerJpa;
import com.group11.accord.jpa.user.AccountJpa;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChannelService {
    private AuthorizationService authorizationService;
    private ServerService serverService;

    private ChannelRepository channelRepository;
    private ServerChannelRepository serverChannelRepository;
    private UserChannelRepository userChannelRepository;

    public Channel createServerChannel(Long serverId, String channelName, Long accountId, String token) {
        authorizationService.validateSession(accountId, token);

        ServerJpa server = serverService.validateOwner(serverId, accountId, token);

        return serverChannelRepository.save(ServerChannelJpa.create(server, channelName)).toDto();
    }

    public List<Channel> getDmChannels(Long accountId, String token) {
        AccountJpa accountJpa = authorizationService.findValidAccount(accountId, token);

        //This a little goofy but I don't know how else to do it
        List<UserChannelJpa> userChannels = userChannelRepository.findAllByIdAccountOneOrIdAccountTwo(accountJpa, accountJpa);

        return userChannels
                .stream()
                .map(UserChannelJpa::toDto)
                .toList();
    }

    public List<Channel> getServerChannels(Long serverId, Long accountId, String token) {
        authorizationService.validateSession(accountId, token);

        serverService.verifyIsServerMember(accountId, serverId);

        return serverChannelRepository.findAllByIdServerId(serverId)
                .stream()
                .map(ServerChannelJpa::toDto)
                .toList();
    }

    public List<Message> getChannelMessages(Long channelId, Long accountId, String token) {
        AccountJpa accountJpa = authorizationService.findValidAccount(accountId, token);

        ChannelJpa channelJpa = channelRepository.findById(channelId)
                .orElseThrow(()-> new EntityNotFoundException(ErrorMessages.MISSING_CHANNEL_WITH_ID.formatted(channelId)));

        //Finding if this channel is part of a server
       if (serverChannelRepository.existsByIdChannelId(channelId)) {
           ServerChannelJpa serverChannel = serverChannelRepository.findByIdChannelId(channelId);

           serverService.verifyIsServerMember(accountId, serverChannel.getId().getServer().getId());

           return channelJpa.getMessages()
                   .stream()
                   .map(MessageJpa::toDto)
                   .toList();

       } else if (userChannelRepository.existsByIdChannelId(channelId)) {

       }

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
