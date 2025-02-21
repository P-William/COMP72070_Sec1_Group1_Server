package com.group11.accord.jpa.server;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.api.server.BasicServer;
import com.group11.accord.api.server.Server;
import com.group11.accord.jpa.channel.ChannelJpa;
import com.group11.accord.jpa.user.AccountJpa;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@ToString(exclude = "channels")
@Table(name = "server")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ServerJpa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_id_seq")
    @SequenceGenerator(name = "server_id_seq", sequenceName = "server_id_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @Column
    private String name;

    @NonNull
    @OneToOne
    @JoinColumn(name = "owner_id")
    private AccountJpa owner;

    @Setter(AccessLevel.NONE)
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "server_channel",
            joinColumns = @JoinColumn(name = "server_id"),
            inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    private List<ChannelJpa> channels = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "server_member",
            joinColumns = @JoinColumn(name = "server_id"),
            inverseJoinColumns = @JoinColumn(name = "account_id")
    )
    private List<AccountJpa> members = new ArrayList<>();

    @NonNull
    @Column
    private LocalDateTime createdAt;

    public static ServerJpa create(String name, AccountJpa owner) {
        return ServerJpa.builder()
                .name(name)
                .owner(owner)
                .createdAt(LocalDateTime.now())
                .build();
    }

//    public Server toDto(){
//        return new Server(id, name, owner.toDto(), createdAt,
//                channels.stream().map(ChannelJpa::toDto).toList(),
//                members.stream().map(AccountJpa::toDto).toList());
//    }

    public BasicServer toBasicDto() {
        return new BasicServer(id, name, owner.toDto());
    }
}
