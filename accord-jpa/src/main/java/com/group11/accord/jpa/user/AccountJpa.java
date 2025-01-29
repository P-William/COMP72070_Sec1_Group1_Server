package com.group11.accord.jpa.user;

import com.group11.accord.jpa.ChannelJpa;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// TODO: placeholder for Connor
@Getter
@Entity
@Table(name = "account")
public class AccountJpa implements Serializable {

    @Id
    Long id;

    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
        name = "user_channel",
        joinColumns = @JoinColumn(name = "account_one_id"),
        inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    private List<ChannelJpa> directMessages = new ArrayList<>();
}
