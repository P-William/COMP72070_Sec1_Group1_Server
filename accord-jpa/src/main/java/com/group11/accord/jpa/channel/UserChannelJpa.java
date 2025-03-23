package com.group11.accord.jpa.channel;

import com.group11.accord.api.channel.Channel;
import com.group11.accord.jpa.message.MessageJpa;
import com.group11.accord.jpa.user.AccountJpa;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@Entity
@Builder
@Table(name = "user_channel")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class UserChannelJpa {

    @EmbeddedId
    private UserChannelId id;

    public static UserChannelJpa create(AccountJpa accountOne, AccountJpa accountTwo, String channelName) {
        return UserChannelJpa.builder()
                .id(new UserChannelId(accountOne, accountTwo, ChannelJpa.create(channelName, true)))
                .build();
    }

    public Channel toDto() {
        return new Channel(
                id.getChannel().getId(),
                id.getChannel().getName(),
                id.getChannel().getMessages().stream().map(MessageJpa::toDto).toList(),
                true,
                id.getChannel().getCreatedAt()
        );
    }
}
