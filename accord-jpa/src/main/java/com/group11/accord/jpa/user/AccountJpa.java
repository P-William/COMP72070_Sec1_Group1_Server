package com.group11.accord.jpa.user;

import jakarta.persistence.*;
import lombok.*;
import com.group11.accord.jpa.channel.ChannelJpa;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

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
    private String profile_url;

    @Setter(AccessLevel.NONE)
    @ManyToMany
    @JoinTable(
        name = "user_channel",
        joinColumns = @JoinColumn(name = "account_one_id"),
        inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    private List<ChannelJpa> directMessages = new ArrayList<>();
}
