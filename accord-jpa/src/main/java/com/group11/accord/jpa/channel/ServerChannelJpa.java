package com.group11.accord.jpa.channel;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.jpa.message.MessageJpa;
import com.group11.accord.jpa.server.ServerJpa;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

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

    public static ServerChannelJpa create(ServerJpa server, String channelName) {
        return ServerChannelJpa.builder()
                .id(new ServerChannelId(server, ChannelJpa.create(channelName)))
                .build();
    }

    public Channel toDto() {
        return new Channel(
                id.getChannel().getId(),
                id.getChannel().getName(),
                id.getChannel().getMessages().stream().map(MessageJpa::toDto).toList(),
                false,
                id.getChannel().getCreatedAt()
        );
    }
}
