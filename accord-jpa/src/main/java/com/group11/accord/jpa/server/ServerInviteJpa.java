package com.group11.accord.jpa.server;

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
    @Column(name = "sent_at")
    private LocalDateTime sentAt;
}
