package com.group11.accord.jpa;

import com.group11.accord.jpa.user.AccountJpa;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@ToString(exclude = "channels")
@Table(name = "server")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ServerJpa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "server_id_seq")
    @SequenceGenerator(name = "server_id_seq", sequenceName = "server_id_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @Column
    private String name;

    @NonNull
    @OneToOne
    @JoinColumn(name = "owner_id")
    private AccountJpa owner;


//    TODO: Need to decide how to handle channels vs direct messages
//    @Setter(AccessLevel.NONE)
//    @Builder.Default
//    private List<ChannelJpa> channels = new ArrayList<>();

    @NonNull
    @Column
    private LocalDateTime createdAt;
}
