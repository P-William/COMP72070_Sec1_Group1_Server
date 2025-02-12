package com.group11.accord.jpa.user.friend;

import com.group11.accord.jpa.user.AccountJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequestJpa, Long> {

    boolean existsBySender(AccountJpa sender);
    boolean existsByReceiver(AccountJpa receiver);

    boolean existsBySenderAndReceiver(AccountJpa sender, AccountJpa receiver);

    List<FriendRequestJpa> findAllBySender(AccountJpa sender);
    List<FriendRequestJpa> findAllByReceiver(AccountJpa receiver);
}
