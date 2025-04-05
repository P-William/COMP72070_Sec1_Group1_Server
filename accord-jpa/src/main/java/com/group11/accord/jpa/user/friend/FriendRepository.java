package com.group11.accord.jpa.user.friend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FriendRepository extends JpaRepository<FriendJpa, Long> {

    boolean existsById(FriendId id);
}
