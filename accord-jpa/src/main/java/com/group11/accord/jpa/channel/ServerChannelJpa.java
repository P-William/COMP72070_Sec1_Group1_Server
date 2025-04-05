package com.group11.accord.jpa.channel;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.jpa.message.MessageJpa;
import com.group11.accord.jpa.server.ServerJpa;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.Hibernate;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Getter
@Setter
@Entity
@Builder
@Table(name = "server_channel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ServerChannelJpa {

    @EmbeddedId
    private ServerChannelId id;

    public static ServerChannelJpa create(ServerJpa server, ChannelJpa channelJpa) {
        return ServerChannelJpa.builder()
                .id(new ServerChannelId(server, channelJpa))
                .build();
    }

    public Channel toDto() {
        return new Channel(
                id.getChannel().getId(),
                id.getChannel().getName(),
                Optional.ofNullable(id.getChannel().getMessages())
                        .orElse(List.of())
                        .stream()
                        .map(MessageJpa::toDto)
                        .toList(),
                false,
                id.getChannel().getCreatedAt()
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        ServerChannelJpa serverChannelJpa = (ServerChannelJpa) o;
        return id.equals(serverChannelJpa.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
