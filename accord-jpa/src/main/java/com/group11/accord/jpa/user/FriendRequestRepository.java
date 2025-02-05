package com.group11.accord.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequestJpa, Long> {

    boolean existsBySender(AccountJpa sender);
    boolean existsByReceiver(AccountJpa receiver);

    List<FriendRequestJpa> findAllBySender(AccountJpa sender);
    List<FriendRequestJpa> findAllByReceiver(AccountJpa receiver);
}
