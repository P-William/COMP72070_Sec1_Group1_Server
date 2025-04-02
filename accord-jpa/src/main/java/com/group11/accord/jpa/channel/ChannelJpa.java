package com.group11.accord.jpa.channel;

import com.group11.accord.jpa.message.MessageJpa;
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
@Table(name = "channel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ChannelJpa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "channel_id_seq")
    @SequenceGenerator(name = "channel_id_seq", sequenceName = "channel_id_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @Column
    private String name;

    @NonNull
    @Column
    @Builder.Default
    private Boolean isPrivate = false;

    @NonNull
    @Column
    LocalDateTime createdAt;

    @OneToMany(mappedBy = "channel")
    private List<MessageJpa> messages = new ArrayList<>();

    public static ChannelJpa create(String name, Boolean isPrivate) {
        return ChannelJpa.builder()
            .name(name)
                .isPrivate(isPrivate)
                .createdAt(LocalDateTime.now())
            .build();
    }


}
