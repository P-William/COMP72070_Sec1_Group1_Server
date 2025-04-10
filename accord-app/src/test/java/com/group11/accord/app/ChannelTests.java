package com.group11.accord.app;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.api.message.Message;
import com.group11.accord.api.message.NewTextMessage;
import com.group11.accord.app.services.AuthorizationService;
import com.group11.accord.app.services.ChannelService;
import com.group11.accord.app.services.FileService;
import com.group11.accord.app.services.ServerService;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChannelTests {
    @Mock
    private FriendRepository friendRepository;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private ServerService serverService;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ServerChannelRepository serverChannelRepository;
    @Mock
    private UserChannelRepository userChannelRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private FileService fileService;
    @Mock
    private ChannelPublisher channelPublisher;
    @Mock
    private MessagePublisher messagePublisher;

    @InjectMocks
    private ChannelService channelService;

    @Test
    void changeChannelName_Success() {
        Long id = 1L;
        String token = "token";

        AccountJpa accountJpa = mock(AccountJpa.class);

        ChannelJpa channelJpa = mock(ChannelJpa.class);

        ServerChannelJpa serverChannelJpa = mock(ServerChannelJpa.class);

        ServerJpa serverJpa = mock(ServerJpa.class);

        when(authorizationService.findValidAccount(id, token)).thenReturn(accountJpa);
        when(channelRepository.findById(id)).thenReturn(Optional.of(channelJpa));
        when(serverChannelRepository.findByIdChannelId(id)).thenReturn(Optional.of(serverChannelJpa));
        when(channelJpa.getIsPrivate()).thenReturn(false);
        when(serverChannelJpa.getId()).thenReturn(new ServerChannelId(serverJpa, channelJpa));
        when(serverChannelJpa.getId().getServer().getId()).thenReturn(1L);

        channelService.changeChannelName(id, "channel", id, token);

        verify(channelRepository).save(channelJpa);
    }

    @Test
    void getMessages_Success() {
        Long id = 1L;
        String token = "token";

        AccountJpa accountJpa = mock(AccountJpa.class);
        AccountJpa friendJpa = mock(AccountJpa.class);
        ChannelJpa channelJpa = mock(ChannelJpa.class);
        UserChannelJpa userChannelJpa = mock(UserChannelJpa.class);

        when(userChannelRepository.findByIdChannelId(id)).thenReturn(Optional.of(userChannelJpa));
        when(userChannelJpa.getId()).thenReturn(new UserChannelId(accountJpa, friendJpa, channelJpa));
        when(authorizationService.findValidAccount(id, token)).thenReturn(accountJpa);
        when(channelJpa.getIsPrivate()).thenReturn(true);
        when(channelRepository.findById(id)).thenReturn(Optional.of(channelJpa));
        when(friendRepository.existsById(any(FriendId.class))).thenReturn(true);

        MessageJpa messageJpa = mock(MessageJpa.class);

        List<MessageJpa> messages = List.of(messageJpa);
        when(channelJpa.getMessages()).thenReturn(messages);

        List<Message> messageList = channelService.getChannelMessages(id, id, token);

        assertNotNull(messageList);
    }

    @Test
    void getDmChannels_Success() {
        Long id = 1L;
        String token = "token";

        AccountJpa accountJpa = mock(AccountJpa.class);

        when(authorizationService.findValidAccount(id, token)).thenReturn(accountJpa);

        UserChannelJpa userChannelJpa = mock(UserChannelJpa.class);
        List<UserChannelJpa> userChannels = List.of(userChannelJpa);

        when(userChannelRepository.findAllByIdAccountOneOrIdAccountTwo(accountJpa, accountJpa)).thenReturn(userChannels);

        List<Channel> channels = channelService.getDmChannels(id, token);

        assertNotNull(channels);
    }

    @Test
    void sendTextMessage_Success() {
        Long id = 1L;
        String token = "token";

        AccountJpa accountJpa = mock(AccountJpa.class);
        ChannelJpa channelJpa = mock(ChannelJpa.class);
        ServerJpa serverJpa = mock(ServerJpa.class);
        ServerChannelJpa serverChannelJpa = mock(ServerChannelJpa.class);
        MessageJpa messageJpa = mock(MessageJpa.class);

        when(authorizationService.findValidAccount(id, token)).thenReturn(accountJpa);
        when(channelRepository.findById(id)).thenReturn(Optional.of(channelJpa));
        when(serverChannelRepository.findByIdChannelId(id)).thenReturn(Optional.of(serverChannelJpa));
        when(channelJpa.getIsPrivate()).thenReturn(false);
        when(serverChannelJpa.getId()).thenReturn(new ServerChannelId(serverJpa, channelJpa));
        when(serverChannelJpa.getId().getServer().getId()).thenReturn(1L);
        when(messageRepository.save(any(MessageJpa.class))).thenReturn(messageJpa);

        NewTextMessage newTextMessage = new NewTextMessage("hello");

        channelService.sendTextMessage(id,newTextMessage,id, token);

        verify(messageRepository).save(any(MessageJpa.class));
    }

    @Test
    void sendImageMessage_Success() {
        Long id = 1L;
        String token = "token";

        AccountJpa accountJpa = mock(AccountJpa.class);
        ChannelJpa channelJpa = mock(ChannelJpa.class);
        ServerJpa serverJpa = mock(ServerJpa.class);
        ServerChannelJpa serverChannelJpa = mock(ServerChannelJpa.class);
        MessageJpa messageJpa = mock(MessageJpa.class);
        MultipartFile multipartFile = mock(MultipartFile.class);

        when(authorizationService.findValidAccount(id, token)).thenReturn(accountJpa);
        when(channelRepository.findById(id)).thenReturn(Optional.of(channelJpa));
        when(serverChannelRepository.findByIdChannelId(id)).thenReturn(Optional.of(serverChannelJpa));
        when(channelJpa.getIsPrivate()).thenReturn(false);
        when(serverChannelJpa.getId()).thenReturn(new ServerChannelId(serverJpa, channelJpa));
        when(serverChannelJpa.getId().getServer().getId()).thenReturn(1L);
        when(messageRepository.save(any(MessageJpa.class))).thenReturn(messageJpa);
        when(fileService.saveImage(any(MultipartFile.class))).thenReturn("success");

        channelService.sendImageMessage(id,multipartFile,id, token);

        verify(messageRepository).save(any(MessageJpa.class));
    }

    @Test
    void createServerChannel_Success() {

    }

    @Test
    void deleteServerChannel_Success() {

    }

}
