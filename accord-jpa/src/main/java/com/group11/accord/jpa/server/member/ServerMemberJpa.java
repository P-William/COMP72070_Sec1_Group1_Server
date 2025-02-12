package com.group11.accord.jpa.server.member;

import com.group11.accord.jpa.server.ServerJpa;
import com.group11.accord.jpa.user.AccountJpa;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Builder
@Table(name = "server_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class ServerMemberJpa {

    @EmbeddedId
    private MemberId id;

    @NonNull
    @Column
    private LocalDateTime joinedAt;

    public static ServerMemberJpa create(AccountJpa account, ServerJpa server){
        return ServerMemberJpa.builder()
                .id(new MemberId(account, server))
                .joinedAt(LocalDateTime.now())
                .build();
    }
}
