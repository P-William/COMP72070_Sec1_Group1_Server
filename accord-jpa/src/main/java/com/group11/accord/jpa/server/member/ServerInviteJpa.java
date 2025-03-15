package com.group11.accord.jpa.server.member;

import com.group11.accord.api.server.members.ServerInvite;
import com.group11.accord.jpa.server.ServerJpa;
import com.group11.accord.jpa.user.AccountJpa;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "server_invite")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ServerInviteJpa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_inv_id_seq")
    @SequenceGenerator(name = "server_inv_id_seq", sequenceName = "server_inv_id_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private AccountJpa sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private AccountJpa receiver;

    @ManyToOne
    @JoinColumn(name = "server_id", nullable = false)
    private ServerJpa server;

    @NonNull
    @Column
    private LocalDateTime sentAt;

    public static ServerInviteJpa create(AccountJpa sender, AccountJpa receiver, ServerJpa server){
        return ServerInviteJpa.builder()
                .sender(sender)
                .receiver(receiver)
                .server(server)
                .sentAt(LocalDateTime.now())
                .build();
    }

    public ServerInvite toDto() {
        return new ServerInvite(id, sender.toDto(), server.toBasicDto(), sentAt);
    }
}
