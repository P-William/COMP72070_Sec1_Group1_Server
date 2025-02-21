package com.group11.accord.jpa.user;

import com.group11.accord.api.user.Account;
import com.group11.accord.jpa.channel.ChannelJpa;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// TODO: placeholder for Connor
@Getter
@Setter
@Entity
@Builder
@Table(name = "account")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class AccountJpa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_id_seq")
    @SequenceGenerator(name = "account_id_seq", sequenceName = "account_id_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @Column(unique = true)
    private String username;

    @NonNull
    @Column
    private String password;

    @NonNull
    @Column
    private String salt;

    @NonNull
    @Column
    private LocalDateTime createdAt;

    @NonNull
    @Column
    private Boolean deleted;

    @Column
    private String bio;

    @Column
    private String profilePicUrl;

    @Setter(AccessLevel.NONE)
    @Builder.Default
    @ManyToMany
    @JoinTable(
        name = "user_channel",
        joinColumns = @JoinColumn(name = "account_one_id"),
        inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    private List<ChannelJpa> directMessages = new ArrayList<>();

    @Setter(AccessLevel.NONE)
    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "friend",
            joinColumns = @JoinColumn(name = "sender_id"),
            inverseJoinColumns = @JoinColumn(name = "friend_id")
    )
    private List<AccountJpa> friends = new ArrayList<>();

    public static AccountJpa create(String username, String password, String salt) {
        return AccountJpa.builder()
                .username(username)
                .password(password)
                .salt(salt)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public Account toDto(){
        return new Account(id, username, bio, profilePicUrl);
    }
}
