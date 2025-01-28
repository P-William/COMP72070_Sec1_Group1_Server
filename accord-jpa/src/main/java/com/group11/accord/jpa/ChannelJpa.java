package com.group11.accord.jpa;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

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
    String name;

    @NonNull
    @Column
    Boolean isPrivate;

    @NonNull
    @Column
    LocalDateTime createdAt;
}
