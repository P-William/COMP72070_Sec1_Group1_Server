package com.group11.accord.jpa.channel;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.jpa.message.MessageJpa;
import com.group11.accord.jpa.server.ServerJpa;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@Table(name = "channel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelJpa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "channel_id_seq")
    @SequenceGenerator(name = "channel_id_seq", sequenceName = "channel_id_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @Column
    String name;

    @NonNull
    @Column
    LocalDateTime createdAt;

    @OneToMany(mappedBy = "channel")
    private List<MessageJpa> messages;

    /*public static ChannelJpa create(ServerJpa server, String name) {

    }*/

//    public Channel toDto(){
//        return new Channel(id, server.getId(), name, isPrivate, createdAt);
//    }
}
