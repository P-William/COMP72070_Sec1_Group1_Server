package com.group11.accord.jpa.server.member;

import com.group11.accord.jpa.server.ServerJpa;
import com.group11.accord.jpa.user.AccountJpa;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "server_kick")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ServerKickJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_kick_id_seq")
    @SequenceGenerator(name = "server_kick_id_seq", sequenceName = "server_kick_id_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @OneToOne
    @JoinColumn(name = "server_id")
    private ServerJpa server;

    @NonNull
    @OneToOne
    @JoinColumn(name = "kicked_user_id")
    private AccountJpa kickedUser;

    @NonNull
    @OneToOne
    @JoinColumn(name = "kicked_by_id")
    private AccountJpa kickedBy;

    @NonNull
    @Column
    private String reason;

    @NonNull
    @Column
    private LocalDateTime kickedAt;

    public static ServerKickJpa create(ServerJpa server, AccountJpa kickedUser, AccountJpa kickedBy, String reason){
        return ServerKickJpa.builder()
                .server(server)
                .kickedUser(kickedUser)
                .kickedBy(kickedBy)
                .reason(reason)
                .kickedAt(LocalDateTime.now())
                .build();
    }
}
