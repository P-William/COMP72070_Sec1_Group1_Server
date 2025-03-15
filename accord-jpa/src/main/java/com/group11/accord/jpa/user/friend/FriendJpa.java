package com.group11.accord.jpa.user.friend;

import com.group11.accord.jpa.user.AccountJpa;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "friend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendJpa {

    @EmbeddedId
    private FriendId id;

    public static FriendJpa create(AccountJpa sender, AccountJpa friend){
        return FriendJpa.builder()
                .id(new FriendId(sender, friend))
                .build();
    }
}
