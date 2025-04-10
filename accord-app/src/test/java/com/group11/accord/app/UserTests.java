package com.group11.accord.app;

import com.group11.accord.api.server.members.ServerInvite;
import com.group11.accord.api.user.Account;
import com.group11.accord.api.user.friend.FriendRequest;
import com.group11.accord.app.services.AccountService;
import com.group11.accord.app.services.AuthorizationService;
import com.group11.accord.app.websockets.ServerPublisher;
import com.group11.accord.app.websockets.UserPublisher;
import com.group11.accord.jpa.channel.ChannelJpa;
import com.group11.accord.jpa.channel.ChannelRepository;
import com.group11.accord.jpa.channel.UserChannelJpa;
import com.group11.accord.jpa.channel.UserChannelRepository;
import com.group11.accord.jpa.server.ServerJpa;
import com.group11.accord.jpa.server.member.ServerInviteJpa;
import com.group11.accord.jpa.server.member.ServerInviteRepository;
import com.group11.accord.jpa.server.member.ServerMemberJpa;
import com.group11.accord.jpa.server.member.ServerMemberRepository;
import com.group11.accord.jpa.user.AccountJpa;
import com.group11.accord.jpa.user.AccountRepository;
import com.group11.accord.jpa.user.friend.FriendId;
import com.group11.accord.jpa.user.friend.FriendRepository;
import com.group11.accord.jpa.user.friend.FriendRequestJpa;
import com.group11.accord.jpa.user.friend.FriendRequestRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private FriendRepository friendRepository;

    @Mock
    private FriendRequestRepository friendRequestRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private UserChannelRepository userChannelRepository;

    @Mock
    private ServerInviteRepository serverInviteRepository;

    @Mock
    private ServerMemberRepository serverMemberRepository;

    @Mock
    private UserPublisher userPublisher;

    @Mock
    private ServerPublisher serverPublisher;

    @InjectMocks
    private AccountService accountService;

    @Test
    void updateUsername_Success() {
        Long id = 1L;
        String token = "token";
        String newUsername = "user";

        AccountJpa accountJpa = mock(AccountJpa.class);
        when(accountRepository.findById(id)).thenReturn(Optional.of(accountJpa));
        when(accountRepository.existsByUsername(newUsername)).thenReturn(false);

        accountService.updateUsername(id, token, newUsername);

        verify(accountRepository).save(accountJpa);
    }

    @Test
    void getAccount_Success() {
        Long id = 1L;
        String token = "token";

        AccountJpa accountJpa = AccountJpa.builder()
                .id(id)
                .username("username")
                .password("password")
                .salt("salt")
                .createdAt(LocalDateTime.now())
                .build();

        when(accountRepository.findById(id)).thenReturn(Optional.of(accountJpa));

        Account account = accountService.getAccount(id, token);

        assertNotNull(account);
    }

    @Test
    void sendFriendRequest_Success() {
        Long id = 1L;
        String token = "token";
        String username = "bob";

        AccountJpa sender = AccountJpa.builder()
                .id(id)
                .username("tom")
                .password("123456")
                .salt("salt")
                .createdAt(LocalDateTime.now())
                .build();

        AccountJpa receiver = AccountJpa.builder()
                .id(id++)
                .username(username)
                .password("123456")
                .salt("salt")
                .createdAt(LocalDateTime.now())
                .build();

        FriendRequestJpa friendRequestJpa = FriendRequestJpa.builder()
                .id(id)
                .sender(sender)
                .receiver(receiver)
                .sentAt(LocalDateTime.now())
                .build();

        when(accountRepository.findById(id)).thenReturn(Optional.of(sender));
        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(receiver));

        when(friendRepository.existsById(any(FriendId.class))).thenReturn(false);
        when(friendRequestRepository.existsBySenderAndReceiver(any(AccountJpa.class), any(AccountJpa.class))).thenReturn(false);

        try(MockedStatic<FriendRequestJpa> mockedStatic = Mockito.mockStatic(FriendRequestJpa.class)) {
            mockedStatic.when(() -> FriendRequestJpa.create(sender, receiver)).thenReturn(friendRequestJpa);

            accountService.sendFriendRequest(id, token, username);

            verify(friendRequestRepository).save(any(FriendRequestJpa.class));
        }
    }

    @Test
    void acceptFriendRequest_Success() {
        Long id = 1L;
        String token = "token";
        String username = "bob";

        AccountJpa sender = AccountJpa.builder()
                .id(id)
                .username("tom")
                .password("123456")
                .salt("salt")
                .createdAt(LocalDateTime.now())
                .build();

        AccountJpa friend = AccountJpa.builder()
                .id(2L)
                .username(username)
                .password("123456")
                .salt("salt")
                .createdAt(LocalDateTime.now())
                .build();

        FriendRequestJpa friendRequestJpa = FriendRequestJpa.builder()
                .id(id)
                .sender(sender)
                .receiver(friend)
                .sentAt(LocalDateTime.now())
                .build();

        when(accountRepository.findById(id)).thenReturn(Optional.of(sender));
        when(friendRequestRepository.findById(id)).thenReturn(Optional.of(friendRequestJpa));

        when(friendRepository.existsById(any(FriendId.class))).thenReturn(false);

        accountService.acceptFriendRequest(id, token, id);

        verify(channelRepository).save(any(ChannelJpa.class));
        verify(userChannelRepository).save(any(UserChannelJpa.class));
    }

    @Test
    void getFriendRequests_Success() {
        Long id = 1L;
        Long id2 = 2L;
        String token = "token";

        AccountJpa accountJpa = AccountJpa.builder()
                .id(id)
                .username("tom")
                .password("123456")
                .salt("salt")
                .createdAt(LocalDateTime.now())
                .build();

        AccountJpa friend = AccountJpa.builder()
                .id(id2)
                .username("bob")
                .password("123456")
                .salt("salt")
                .createdAt(LocalDateTime.now())
                .build();

        FriendRequestJpa friendRequestJpa = FriendRequestJpa.builder()
                .id(id)
                .sender(accountJpa)
                .receiver(friend)
                .sentAt(LocalDateTime.now())
                .build();

        List<FriendRequestJpa> requests = new ArrayList<>();
        requests.add(friendRequestJpa);

        when(accountRepository.findById(id)).thenReturn(Optional.of(accountJpa));
        when(friendRequestRepository.findAllByReceiver(accountJpa)).thenReturn(requests);

        List<FriendRequest> friendRequests = accountService.getFriendRequests(id, token);

        assertNotNull(friendRequests);
    }

    @Test
    void getFriends_Success() {
        Long id = 1L;
        Long id2 = 2L;
        String token = "token";

        AccountJpa accountJpa = mock(AccountJpa.class);

        AccountJpa friend = AccountJpa.builder()
                .id(id2)
                .username("bob")
                .password("123456")
                .salt("salt")
                .createdAt(LocalDateTime.now())
                .build();

        List<AccountJpa> friends = new ArrayList<>();
        friends.add(friend);

        when(accountRepository.findById(id)).thenReturn(Optional.of(accountJpa));
        when(accountJpa.getFriends()).thenReturn(friends);

        List<Account> friendsList = accountService.getFriends(id, token);

        assertNotNull(friendsList);
    }

    @Test
    void getServerInvites_Success() {
        Long id = 1L;
        String token = "token";

        AccountJpa accountJpa = mock(AccountJpa.class);

        ServerInviteJpa serverInviteJpa = mock(ServerInviteJpa.class);

        List<ServerInviteJpa> serverInvites = new ArrayList<>();
        serverInvites.add(serverInviteJpa);

        when(accountRepository.findById(id)).thenReturn(Optional.of(accountJpa));
        when(serverInviteRepository.findAllByReceiver(accountJpa)).thenReturn(serverInvites);

        List<ServerInvite> serverInvitesList = accountService.getServerInvites(id, token);
        assertNotNull(serverInvitesList);
    }

    @Test
    void acceptServerInvites_Success() {
        Long id = 1L;
        String token = "token";

        AccountJpa accountJpa = AccountJpa.builder()
                                      .id(id)
                                      .username("tom")
                                      .password("123456")
                                      .salt("salt")
                                      .createdAt(LocalDateTime.now())
                                      .build();

        ServerMemberJpa serverMemberJpa = mock(ServerMemberJpa.class);

        ServerJpa serverJpa = ServerJpa.builder()
                .id(id)
                .name("Cool server")
                .owner(accountJpa)
                .createdAt(LocalDateTime.now())
                .build();

        ServerInviteJpa serverInviteJpa = ServerInviteJpa.builder()
                .id(id)
                .sender(accountJpa)
                .server(serverJpa)
                .receiver(accountJpa)
                .sentAt(LocalDateTime.now())
                .build();

        when(accountRepository.findById(id)).thenReturn(Optional.of(accountJpa));
        when(serverInviteRepository.findById(id)).thenReturn(Optional.of(serverInviteJpa));

        when(serverMemberRepository.existsByIdAccountIdAndIdServerId(any(Long.class), any(Long.class))).thenReturn(false);

        try (MockedStatic<ServerMemberJpa> mockedStatic = mockStatic(ServerMemberJpa.class)) {
            mockedStatic.when(() -> ServerMemberJpa.create(any(), any())).thenReturn(serverMemberJpa);

            accountService.acceptServerInvite(id, id, token);

            verify(serverMemberRepository).save(serverMemberJpa);
        }

    }
}
