package com.group11.accord.jpa.user;

import com.group11.accord.api.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AccountJpa, Long> {

    boolean existsByUsername(String username);

    boolean existsByFriendOf(AccountJpa friend);

    Optional<AccountJpa> findByUsername(String username);
}
