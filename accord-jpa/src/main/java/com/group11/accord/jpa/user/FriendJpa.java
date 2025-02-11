package com.group11.accord.jpa.user;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "friend")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendJpa {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friend_id_seq")
    @SequenceGenerator(name = "friend_id_seq", sequenceName = "friend_id_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "account_id")
    private AccountJpa owner;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "friend_id")
    private AccountJpa friendId;

}
