package com.group11.accord.app.services;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.api.message.Message;
import com.group11.accord.api.message.NewTextMessage;
import com.group11.accord.app.exceptions.AccountNotAuthorizedException;
import com.group11.accord.app.exceptions.ErrorMessages;
import com.group11.accord.app.exceptions.InvalidOperationException;
import com.group11.accord.app.websockets.ChannelPublisher;
import com.group11.accord.app.websockets.MessagePublisher;
import com.group11.accord.jpa.channel.*;
import com.group11.accord.jpa.message.MessageJpa;
import com.group11.accord.jpa.message.MessageRepository;
import com.group11.accord.jpa.message.MessageType;
import com.group11.accord.jpa.server.ServerJpa;
import com.group11.accord.jpa.user.AccountJpa;
import com.group11.accord.jpa.user.friend.FriendId;
import com.group11.accord.jpa.user.friend.FriendRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ChannelService {
    private final FriendRepository friendRepository;
    private final AuthorizationService authorizationService;
    private final ServerService serverService;

    private final ChannelRepository channelRepository;
    private final ServerChannelRepository serverChannelRepository;
    private final UserChannelRepository userChannelRepository;
    private final MessageRepository messageRepository;
    private final FileService fileService;
    private final ChannelPublisher channelPublisher;
    private final MessagePublisher messagePublisher;

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

        if (!channelJpa.getIsPrivate()){
            ServerChannelJpa serverChannelJpa = getValidServerChannel(channelId);
            serverService.verifyIsServerMember(accountId, serverChannelJpa.getId().getServer().getId());

        } else if (!verifyFriendship(channelId, accountJpa)) {
            throw new EntityNotFoundException(ErrorMessages.NOT_FRIENDS.formatted(accountJpa.getUsername()));
        }

        return channelJpa.getMessages()
                .stream()
                .map(MessageJpa::toDto)
                .toList();
    }

    public void changeChannelName(Long channelId, String newName, Long accountId, String token) {
        AccountJpa accountJpa = authorizationService.findValidAccount(accountId, token);

        ChannelJpa channelJpa = getValidChannel(channelId);

        if (!channelJpa.getIsPrivate()){
            ServerChannelJpa serverChannelJpa = getValidServerChannel(channelId);

            serverService.validateOwner(serverChannelJpa.getId().getServer().getId(), accountId, token);

            channelJpa.setName(newName);
            channelRepository.save(channelJpa);

            channelPublisher.publishChannelEdit(serverChannelJpa.getId().getServer().getId(), serverChannelJpa.toDto());

        } else if (!verifyFriendship(channelId, accountJpa)) {
            throw new EntityNotFoundException(ErrorMessages.NOT_FRIENDS.formatted(accountJpa.getUsername()));
        } else {
            throw new InvalidOperationException(ErrorMessages.INVALID_CHANNEL_NAME_REQUEST);
        }
    }

    public void deleteServerChannel(Long channelId, Long accountId, String token) {
        authorizationService.validateSession(accountId, token);

        ChannelJpa channelJpa = getValidChannel(channelId);

        ServerChannelJpa serverChannelJpa = getValidServerChannel(channelId);

        serverService.validateOwner(serverChannelJpa.getId().getServer().getId(), accountId, token);

        channelPublisher.publishChannelDelete(serverChannelJpa.getId().getServer().getId(), serverChannelJpa.toDto());

        channelRepository.delete(channelJpa);
    }

    public void sendTextMessage(Long channelId, NewTextMessage newMessage, Long accountId, String token) {
        AccountJpa accountJpa = authorizationService.findValidAccount(accountId, token);

        ChannelJpa channelJpa = getValidChannel(channelId);

        Long serverId = null;
        if (!channelJpa.getIsPrivate()){
            ServerChannelJpa serverChannelJpa = getValidServerChannel(channelId);
            serverId = serverChannelJpa.getId().getServer().getId();
            serverService.verifyIsServerMember(accountId, serverChannelJpa.getId().getServer().getId());
        }
        else if (!verifyFriendship(channelId, accountJpa)){
            throw new EntityNotFoundException(ErrorMessages.NOT_FRIENDS.formatted(accountJpa.getUsername()));
        }

        MessageJpa messageJpa = messageRepository.save(MessageJpa.create(accountJpa, newMessage.content(), channelJpa, MessageType.TEXT));
        if (serverId != null) {
            messagePublisher.publishMessage(serverId, messageJpa.toDto());
        } else {
            messagePublisher.publishDmMessage(messageJpa.toDto());
        }
    }

    public void sendImageMessage(Long channelId, MultipartFile image, Long accountId, String token) {
        AccountJpa accountJpa = authorizationService.findValidAccount(accountId, token);

        ChannelJpa channelJpa = getValidChannel(channelId);

        Long serverId = null;
        if (!channelJpa.getIsPrivate()){
            ServerChannelJpa serverChannelJpa = getValidServerChannel(channelId);
            serverId = serverChannelJpa.getId().getServer().getId();
            serverService.verifyIsServerMember(accountId, serverChannelJpa.getId().getServer().getId());
        }
        else if (!verifyFriendship(channelId, accountJpa)){
            throw new EntityNotFoundException(ErrorMessages.NOT_FRIENDS.formatted(accountJpa.getUsername()));
        }

        MessageJpa messageJpa = messageRepository.save(MessageJpa.create(accountJpa, fileService.saveImage(image), channelJpa, MessageType.IMAGE));
        messagePublisher.publishMessage(serverId, messageJpa.toDto());
    }

    public ChannelJpa getValidChannel(Long channelId) {
        return channelRepository.findById(channelId)
                .orElseThrow(()-> new EntityNotFoundException(ErrorMessages.MISSING_CHANNEL_WITH_ID.formatted(channelId)));
    }

    public ServerChannelJpa getValidServerChannel(Long channelId) {
        Optional<ServerChannelJpa> serverChannelJpa = serverChannelRepository.findByIdChannelId(channelId);
        if (serverChannelJpa.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.CHANNEL_NOT_OF_TYPE.formatted("ServerChannel"));
        }

        return serverChannelJpa.get();
    }

    public UserChannelJpa getValidUserChannel(Long channelId) {
        Optional<UserChannelJpa> userChannelJpa = userChannelRepository.findByIdChannelId(channelId);
        if (userChannelJpa.isEmpty()){
            throw new EntityNotFoundException(ErrorMessages.CHANNEL_NOT_OF_TYPE.formatted("DirectMessageChannel"));
        }

        return  userChannelJpa.get();
    }
    
    public boolean verifyFriendship(Long channelId, AccountJpa sender) {
        UserChannelJpa userChannelJpa = getValidUserChannel(channelId);
        AccountJpa friend = getFriend(sender, userChannelJpa);

        //Redundancy check to ensure the two users are friends
        return friendRepository.existsById(new FriendId(sender, friend)) || friendRepository.existsById(new FriendId(friend, sender));
    }

    private static AccountJpa getFriend(AccountJpa sender, UserChannelJpa userChannel) {
        AccountJpa friend;

        //Check if user is attached to the channel
        if (userChannel.getId().getAccountTwo().equals(sender)) {
            friend = userChannel.getId().getAccountOne();
        } else if (Objects.equals(userChannel.getId().getAccountOne(), sender)) {
            friend = userChannel.getId().getAccountTwo();
        } else {
            throw new AccountNotAuthorizedException(ErrorMessages.INVALID_CHANNEL_AUTHORITY.formatted(sender.getUsername()));
        }
        return friend;
    }

}
