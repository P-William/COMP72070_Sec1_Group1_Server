package com.group11.accord.jpa.user.friend;

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
@AllArgsConstructor
@NoArgsConstructor
public class FriendId {

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private AccountJpa sender;

    @ManyToOne
    @JoinColumn(name = "friend_id")
    private AccountJpa friend;
}
