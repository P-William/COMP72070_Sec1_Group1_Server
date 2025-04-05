package com.group11.accord.jpa.channel;

import com.group11.accord.jpa.server.ServerJpa;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ServerChannelId {

    @ManyToOne
    @JoinColumn(name = "server_id")
    private ServerJpa server;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private ChannelJpa channel;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        ServerChannelId serverChannelId = (ServerChannelId) o;
        return server.equals(serverChannelId.getServer()) && channel.equals(serverChannelId.getChannel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(server, channel);
    }
}
