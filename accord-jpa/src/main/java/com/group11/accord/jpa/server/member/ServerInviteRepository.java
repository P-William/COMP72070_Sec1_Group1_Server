package com.group11.accord.jpa.server.member;

import com.group11.accord.jpa.user.AccountJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerInviteRepository extends JpaRepository<ServerInviteJpa, Long> {

    List<ServerInviteJpa> findAllByReceiver(AccountJpa receiver);

    boolean existsByReceiver(AccountJpa receiver);
}
