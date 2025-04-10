package com.group11.accord.jpa.user.session;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SessionRepository extends JpaRepository<SessionJpa, SessionId> {

    boolean existsByIdAccountIdAndIdToken(Long accountId, String token);

    void deleteAllByIdAccountId(Long accountId);

    void deleteByIdAccountId(Long accountId);

    default boolean sessionExists(Long accountId, String token) {
        return existsByIdAccountIdAndIdToken(accountId, token);
    }
}
