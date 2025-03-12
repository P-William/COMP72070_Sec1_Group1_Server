package com.group11.accord.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountJpa, Long> {

    boolean existsByUsername(String username);

    boolean existsByFriends(AccountJpa friend);

    Optional<AccountJpa> findByUsername(String username);
}
