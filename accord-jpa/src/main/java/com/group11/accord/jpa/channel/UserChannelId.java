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
import org.hibernate.Hibernate;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        UserChannelId userChannelId = (UserChannelId) o;
        return accountOne.equals(userChannelId.getAccountOne()) && accountTwo.equals(userChannelId.getAccountTwo()) && channel.equals(userChannelId.getChannel());
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountOne, accountTwo, channel);
    }
}
