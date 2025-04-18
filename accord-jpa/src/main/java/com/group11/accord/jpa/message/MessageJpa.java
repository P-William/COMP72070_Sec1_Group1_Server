package com.group11.accord.jpa.message;

import com.group11.accord.api.message.Message;
import com.group11.accord.jpa.channel.ChannelJpa;
import com.group11.accord.jpa.user.AccountJpa;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "message")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class MessageJpa implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "message_id_seq")
    @SequenceGenerator(name = "message_id_seq", sequenceName = "message_id_seq", allocationSize = 1)
    @Setter(AccessLevel.NONE)
    private Long id;

    @NonNull
    @OneToOne
    @JoinColumn(name = "author_id")
    private AccountJpa author;

    @NonNull
    @Column
    private String content;

    @NonNull
    @Column
    private LocalDateTime sentAt;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "channel_id")
    private ChannelJpa channel;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column
    private MessageType type;

    public static MessageJpa create(AccountJpa accountJpa, String content, ChannelJpa channelJpa, MessageType type) {
        return MessageJpa.builder()
                .author(accountJpa)
                .content(content)
                .channel(channelJpa)
                .sentAt(LocalDateTime.now())
                .type(type)
                .build();
    }

    public Message toDto() {
        boolean isImage = type.equals(MessageType.IMAGE);

        return new Message(id, author.toDto(), channel.getId(), content, isImage, sentAt);
    }
}
