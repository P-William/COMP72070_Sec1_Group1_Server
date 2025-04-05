package com.group11.accord.jpa.server.member;

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
public class MemberId {

    @ManyToOne
    @JoinColumn(name = "account_id")
    private AccountJpa account;

    @ManyToOne
    @JoinColumn(name = "server_id")
    private ServerJpa server;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }

        MemberId memberId = (MemberId) o;
        return account.equals(memberId.getAccount()) && server.equals(memberId.getServer());
    }

    @Override
    public int hashCode() {
        return Objects.hash(account, server);
    }
}
