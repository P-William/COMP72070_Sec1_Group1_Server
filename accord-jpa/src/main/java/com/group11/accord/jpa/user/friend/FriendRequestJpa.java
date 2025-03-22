package com.group11.accord.jpa.user.friend;

import com.group11.accord.api.user.friend.FriendRequest;
import com.group11.accord.jpa.user.AccountJpa;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "friend_request")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class FriendRequestJpa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "friend_req_id_seq")
    @SequenceGenerator(name = "friend_req_id_seq", sequenceName = "friend_req_id_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    private AccountJpa sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private AccountJpa receiver;

    @NonNull
    @Column
    private LocalDateTime sentAt;

    public static FriendRequestJpa create(AccountJpa sender, AccountJpa receiver) {
        return FriendRequestJpa.builder()
                .sender(sender)
                .receiver(receiver)
                .sentAt(LocalDateTime.now())
                .build();
    }

    public FriendRequest toDto() {
        return new FriendRequest(id, sender.toDto(), receiver.toDto(), sentAt);
    }
}
