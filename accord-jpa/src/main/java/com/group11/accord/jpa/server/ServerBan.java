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
@Table(name = "server_ban")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ServerBan implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_ban_id_seq")
    @SequenceGenerator(name = "server_ban_id_seq", sequenceName = "server_ban_id_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @OneToOne
    @JoinColumn(name = "server_id")
    private ServerJpa server;

    @NonNull
    @OneToOne
    @JoinColumn(name = "banned_user_id")
    private AccountJpa bannedUser;

    @NonNull
    @OneToOne
    @JoinColumn(name = "banned_by_id")
    private AccountJpa bannedBy;

    @NonNull
    @Column
    private String reason;

    @NonNull
    @Column
    private LocalDateTime bannedAt;
}
