package com.group11.accord.jpa.channel;

import com.group11.accord.jpa.server.ServerJpa;
import com.group11.accord.jpa.user.AccountJpa;
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
public class UserChannelId {

    @ManyToOne
    @JoinColumn(name = "account_one_id")
    private AccountJpa accountOne;

    @ManyToOne
    @JoinColumn(name = "account_two_id")
    private AccountJpa accountTwo;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private ChannelJpa channel;

}
