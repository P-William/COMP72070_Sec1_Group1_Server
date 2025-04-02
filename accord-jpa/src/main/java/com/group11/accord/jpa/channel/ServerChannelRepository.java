package com.group11.accord.jpa.channel;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServerChannelRepository extends JpaRepository<ServerChannelJpa, Long> {

    List<ServerChannelJpa> findAllByIdServerId(Long serverId);

    Optional<ServerChannelJpa> findByIdChannelId(Long channelId);

    boolean existsByIdChannelId(Long channelId);
}
