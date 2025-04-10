package com.group11.accord.jpa.user.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FriendRepository extends JpaRepository<FriendJpa, Long> {

    Optional<FriendJpa> findById(FriendId friendId);

    boolean existsById(FriendId id);
}
