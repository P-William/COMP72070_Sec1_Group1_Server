package com.group11.accord.app.services;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.api.message.Message;
import com.group11.accord.api.message.NewImageMessage;
import com.group11.accord.api.message.NewTextMessage;
import com.group11.accord.app.exceptions.AccountNotAuthorizedException;
import com.group11.accord.app.exceptions.ErrorMessages;
import com.group11.accord.app.exceptions.ServerErrorException;
import com.group11.accord.jpa.channel.*;
import com.group11.accord.jpa.message.MessageJpa;
import com.group11.accord.jpa.server.ServerJpa;
import com.group11.accord.jpa.user.AccountJpa;
import com.group11.accord.jpa.user.friend.FriendId;
import com.group11.accord.jpa.user.friend.FriendRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChannelService {
    private final FriendRepository friendRepository;
    private final AuthorizationService authorizationService;
    private final ServerService serverService;

    private final ChannelRepository channelRepository;
    private final ServerChannelRepository serverChannelRepository;
    private final UserChannelRepository userChannelRepository;

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

        ChannelJpa channelJpa = getValidChannel(channelId);

        //Finding if this channel is part of a server or dm
       if (checkIsValidServerChannel(channelId, accountId) || checkValidDirectChannel(channelId, accountJpa)){
           return channelJpa.getMessages()
                   .stream()
                   .map(MessageJpa::toDto)
                   .toList();
       }
       else {
           //If this happens it means the server did not link the channel to a server channel or dm channel
           throw new ServerErrorException(ErrorMessages.CHANNEL_NOT_BOUND);
       }
    }

    public void changeChannelName(Long channelId, String newName, Long accountId, String token) {
        AccountJpa accountJpa = authorizationService.findValidAccount(accountId, token);

        ChannelJpa channelJpa = getValidChannel(channelId);

        if (checkIsValidServerChannel(channelId, accountId)){
            ServerChannelJpa serverChannelJpa = serverChannelRepository.findByIdChannelId(channelJpa.getId());

            serverService.validateOwner(serverChannelJpa.getId().getServer().getId(), accountId, token);

            channelJpa.setName(newName);

            channelRepository.save(channelJpa);
        } else if (checkValidDirectChannel(channelId, accountJpa)) {
            channelJpa.setName(newName);
            channelRepository.save(channelJpa);
        }
    }

    public void deleteServerChannel(Long channelId, Long accountId, String token) {
        AccountJpa accountJpa = authorizationService.findValidAccount(accountId, token);

        ChannelJpa channelJpa = getValidChannel(channelId);

        if (checkIsValidServerChannel(channelId, accountId)){
            ServerChannelJpa serverChannelJpa = serverChannelRepository.findByIdChannelId(channelJpa.getId());

            serverService.validateOwner(serverChannelJpa.getId().getServer().getId(), accountId, token);

            channelRepository.delete(channelJpa);
        } else {
            throw new AccountNotAuthorizedException(ErrorMessages.INVALID_CHANNEL_AUTHORITY.formatted(accountJpa.getUsername()));
        }
    }

    public void sendTextMessage(Long channelId, NewTextMessage newMessage, Long accountId, String token) {

    }

    public void sendImageMessage(Long channelId, NewImageMessage newMessage, Long accountId, String token) {

    }

    public ChannelJpa getValidChannel(Long channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(()-> new EntityNotFoundException(ErrorMessages.MISSING_CHANNEL_WITH_ID.formatted(channelId)));
    }
    
    public boolean checkIsValidServerChannel(Long channelId, Long accountId) {
        if (serverChannelRepository.existsByIdChannelId(channelId)) {
            ServerChannelJpa serverChannel = serverChannelRepository.findByIdChannelId(channelId);

            serverService.verifyIsServerMember(accountId, serverChannel.getId().getServer().getId());
            
            return true;
        }
        else {
            return false;
        }
    }
    
    public boolean checkValidDirectChannel(Long channelId, AccountJpa sender) {
        if (userChannelRepository.existsByIdChannelId(channelId)) {//Checking if channel is between friends
            UserChannelJpa userChannel = userChannelRepository.findByIdChannelId(channelId);

            AccountJpa friend = getFriend(sender, userChannel);

            //Redundancy check to ensure the two users are friends
            if (!friendRepository.existsById(new FriendId(sender, friend)) || !friendRepository.existsById(new FriendId(friend, sender))) {
                throw new EntityNotFoundException(ErrorMessages.NOT_FRIENDS.formatted(sender.getUsername()));
            }
            return true;
        }
        else{
            return false;
        }
    }

    private static AccountJpa getFriend(AccountJpa sender, UserChannelJpa userChannel) {
        AccountJpa friend;

        //Check if user is attached to the channel
        if (Objects.equals(userChannel.getId().getAccountTwo(), sender)) {
            friend = userChannel.getId().getAccountOne();
        } else if (Objects.equals(userChannel.getId().getAccountOne(), sender)) {
            friend = userChannel.getId().getAccountTwo();
        } else {
            throw new AccountNotAuthorizedException(ErrorMessages.INVALID_CHANNEL_AUTHORITY.formatted(sender.getUsername()));
        }
        return friend;
    }

}
