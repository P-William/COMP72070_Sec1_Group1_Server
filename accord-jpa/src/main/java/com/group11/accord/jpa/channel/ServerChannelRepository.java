package com.group11.accord.jpa.channel;

import com.group11.accord.jpa.server.ServerJpa;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServerChannelRepository extends JpaRepository<ServerChannelJpa, Long> {

    List<ServerChannelJpa> findAllByIdServerId(Long serverId);

    ServerChannelJpa findByIdChannelId(Long channelId);

    boolean existsByIdChannelId(Long channelId);
}
