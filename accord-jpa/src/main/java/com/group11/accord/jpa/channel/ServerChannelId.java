package com.group11.accord.jpa.channel;

import com.group11.accord.jpa.server.ServerJpa;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

}
