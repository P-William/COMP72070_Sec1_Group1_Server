package com.group11.accord.jpa.server.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerKickRepository extends JpaRepository<ServerKickJpa, Long> {
    boolean existsByServerIdAndKickedUserUsername(Long serverId, String username);

    List<ServerKickJpa> findAllByServerId(Long serverId);

    void deleteByServerIdAndKickedUserUsername(Long serverId, String username);
}
