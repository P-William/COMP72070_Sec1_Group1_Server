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

}
