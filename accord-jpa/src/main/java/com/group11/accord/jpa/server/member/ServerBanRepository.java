package com.group11.accord.jpa.server.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServerBanRepository extends JpaRepository<ServerBanJpa, Long> {

    boolean existsByBannedUserUsernameAndServerId(String bannedUser, Long serverId);

    List<ServerBanJpa> findAllByServerId(Long serverId);

    Optional<ServerBanJpa> findByServerIdAndBannedUserId(Long serverId, Long bannedUserId);
}
