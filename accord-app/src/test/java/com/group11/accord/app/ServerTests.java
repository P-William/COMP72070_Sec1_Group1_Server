package com.group11.accord.app;

import com.group11.accord.api.server.BasicServer;
import com.group11.accord.api.server.members.NewServerBan;
import com.group11.accord.api.server.members.NewServerKick;
import com.group11.accord.app.services.AccountService;
import com.group11.accord.app.services.AuthorizationService;
import com.group11.accord.app.services.ServerService;
import com.group11.accord.app.websockets.ChannelPublisher;
import com.group11.accord.app.websockets.ServerPublisher;
import com.group11.accord.app.websockets.UserPublisher;
import com.group11.accord.jpa.channel.*;
import com.group11.accord.jpa.server.ServerJpa;
import com.group11.accord.jpa.server.ServerRepository;
import com.group11.accord.jpa.server.member.*;
import com.group11.accord.jpa.user.AccountJpa;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ServerTests {
    @Mock
    private ServerKickRepository serverKickRepository;
    @Mock
    private AccountService accountService;
    @Mock
    private ServerMemberRepository serverMemberRepository;
    @Mock
    private ServerBanRepository serverBanRepository;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private ServerRepository serverRepository;
    @Mock
    private ServerInviteRepository serverInviteRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ServerChannelRepository serverChannelRepository;
    @Mock
    private UserPublisher userPublisher;
    @Mock
    private ServerPublisher serverPublisher;
    @Mock
    private ChannelPublisher channelPublisher;

    @InjectMocks
    private ServerService serverService;

    @Test
    void createServer_Success() {
        Long id = 1L;
        String token = "token";

        AccountJpa account = AccountJpa.builder()
                .id(id)
                .username("bob")
                .password("password")
                .salt("salt")
                .createdAt(LocalDateTime.now())
                .build();

        ServerJpa serverJpa = ServerJpa.builder()
                .id(id)
                .name("server")
                .owner(account)
                .createdAt(LocalDateTime.now())
                .build();

        ChannelJpa channelJpa = mock(ChannelJpa.class);
        ServerChannelJpa serverChannelJpa = mock(ServerChannelJpa.class);

        when(authorizationService.findValidAccount(id, token)).thenReturn(account);
        when(serverRepository.save(any(ServerJpa.class))).thenReturn(serverJpa);
        when(serverRepository.existsByOwnerIdAndId(any(Long.class), any(Long.class))).thenReturn(true);
        when(serverChannelRepository.save(any(ServerChannelJpa.class))).thenReturn(serverChannelJpa);
        when(serverChannelJpa.getId()).thenReturn(new ServerChannelId(serverJpa, channelJpa));

        try(MockedStatic<ServerJpa> mockedStatic = Mockito.mockStatic(ServerJpa.class)) {
            mockedStatic.when(()-> ServerJpa.create("server", account)).thenReturn(serverJpa);

            BasicServer server = serverService.createServer(id, token, "server");
            assertNotNull(server);
        }
    }

    @Test
    void getServers_Success() {
        Long id = 1L;
        String token = "token";

        AccountJpa accountJpa = AccountJpa.builder()
                                       .id(id)
                                       .username("bob")
                                       .password("password")
                                       .salt("salt")
                                       .createdAt(LocalDateTime.now())
                                       .build();

        ServerJpa serverJpa = ServerJpa.builder()
                                       .id(id)
                                       .name("server")
                                       .owner(accountJpa)
                                       .createdAt(LocalDateTime.now())
                                       .build();

        List<ServerJpa> servers = List.of(serverJpa);

        when(authorizationService.findValidAccount(id, token)).thenReturn(accountJpa);
        when(serverRepository.findAllByMembersIsContaining(accountJpa)).thenReturn(servers);

        List<BasicServer> basicServers = serverService.getServers(id, token);
        assertNotNull(basicServers);
    }

    @Test
    void changeServerName_Success() {
        Long id = 1L;
        String token = "token";

        AccountJpa accountJpa = AccountJpa.builder()
                                          .id(id)
                                          .username("bob")
                                          .password("password")
                                          .salt("salt")
                                          .createdAt(LocalDateTime.now())
                                          .build();

        ServerJpa serverJpa = ServerJpa.builder()
                                       .id(id)
                                       .name("server")
                                       .owner(accountJpa)
                                       .createdAt(LocalDateTime.now())
                                       .build();

        when(serverRepository.existsByOwnerIdAndId(any(Long.class), any(Long.class))).thenReturn(true);
        when(serverRepository.findByOwnerIdAndId(any(Long.class), any(Long.class))).thenReturn(serverJpa);

        serverService.changeServerName(id, id, token, "new name" );

        verify(serverRepository).save(serverJpa);
        assertEquals("new name", serverJpa.getName());
    }

    @Test
    void deleteServer_Success() {
        Long id = 1L;
        String token = "token";

        AccountJpa accountJpa = AccountJpa.builder()
                                          .id(id)
                                          .username("bob")
                                          .password("password")
                                          .salt("salt")
                                          .createdAt(LocalDateTime.now())
                                          .build();

        ServerJpa serverJpa = ServerJpa.builder()
                                       .id(id)
                                       .name("server")
                                       .owner(accountJpa)
                                       .createdAt(LocalDateTime.now())
                                       .build();

        when(serverRepository.existsByOwnerIdAndId(any(Long.class), any(Long.class))).thenReturn(true);
        when(serverRepository.findByOwnerIdAndId(any(Long.class), any(Long.class))).thenReturn(serverJpa);

        serverService.deleteServer(id, id, token);
        verify(serverRepository).delete(serverJpa);
    }

    @Test
    void kickServer_Success() {
        Long id = 1L;
        Long id2 = 2L;
        String token = "token";

        AccountJpa kickedBy = AccountJpa.builder()
                                          .id(id)
                                          .username("bob")
                                          .password("password")
                                          .salt("salt")
                                          .createdAt(LocalDateTime.now())
                                          .build();

        AccountJpa kickedUser = AccountJpa.builder()
                                          .id(id2)
                                          .username("tom")
                                          .password("password")
                                          .salt("salt")
                                          .createdAt(LocalDateTime.now())
                                          .build();

        ServerJpa serverJpa = ServerJpa.builder()
                                       .id(id)
                                       .name("server")
                                       .owner(kickedBy)
                                       .createdAt(LocalDateTime.now())
                                       .build();

        NewServerKick newServerKick = new NewServerKick(kickedUser.getId(), kickedBy.getId(), "kicked");
        ServerKickJpa serverKickJpa = mock(ServerKickJpa.class);

        when(serverRepository.existsByOwnerIdAndId(any(Long.class), any(Long.class))).thenReturn(true);
        when(serverRepository.findByOwnerIdAndId(any(Long.class), any(Long.class))).thenReturn(serverJpa);
        when(serverMemberRepository.existsByIdAccountIdAndIdServerId(any(Long.class), any(Long.class))).thenReturn(true);
        when(accountService.findAccountWithId(id)).thenReturn(kickedBy);
        when(accountService.findAccountWithId(id2)).thenReturn(kickedUser);

        try(MockedStatic<ServerKickJpa> mockedStatic = Mockito.mockStatic(ServerKickJpa.class)) {
            mockedStatic.when(() -> ServerKickJpa.create(serverJpa, kickedUser, kickedBy, "kicked")).thenReturn(serverKickJpa);

            serverService.kickFromServer(id, id, token, newServerKick);

            verify(serverKickRepository).save(any(ServerKickJpa.class));
        }
    }

    @Test
    void banServer_Success() {
        Long id = 1L;
        Long id2 = 2L;
        String token = "token";

        AccountJpa bannedBy = AccountJpa.builder()
                                        .id(id)
                                        .username("bob")
                                        .password("password")
                                        .salt("salt")
                                        .createdAt(LocalDateTime.now())
                                        .build();

        AccountJpa bannedUser = AccountJpa.builder()
                                          .id(id2)
                                          .username("tom")
                                          .password("password")
                                          .salt("salt")
                                          .createdAt(LocalDateTime.now())
                                          .build();

        ServerJpa serverJpa = ServerJpa.builder()
                                       .id(id)
                                       .name("server")
                                       .owner(bannedBy)
                                       .createdAt(LocalDateTime.now())
                                       .build();

        NewServerBan newServerBan = new NewServerBan(bannedUser.getId(), bannedBy.getId(), "banned");
        ServerBanJpa serverBanJpa = mock(ServerBanJpa.class);

        when(serverRepository.existsByOwnerIdAndId(any(Long.class), any(Long.class))).thenReturn(true);
        when(serverRepository.findByOwnerIdAndId(any(Long.class), any(Long.class))).thenReturn(serverJpa);
        when(serverMemberRepository.existsByIdAccountIdAndIdServerId(any(Long.class), any(Long.class))).thenReturn(true);
        when(accountService.findAccountWithId(id)).thenReturn(bannedBy);
        when(accountService.findAccountWithId(id2)).thenReturn(bannedUser);

        try(MockedStatic<ServerBanJpa> mockedStatic = Mockito.mockStatic(ServerBanJpa.class)) {
            mockedStatic.when(() -> ServerBanJpa.create(serverJpa, bannedUser, bannedBy, "banned")).thenReturn(serverBanJpa);

            serverService.banFromServer(id, id, token, newServerBan);

            verify(serverBanRepository).save(any(ServerBanJpa.class));
        }
    }


}
