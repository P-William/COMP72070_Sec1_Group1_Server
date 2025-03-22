package com.group11.accord.jpa.channel;

import com.group11.accord.jpa.user.AccountJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserChannelRepository extends JpaRepository<UserChannelJpa, Long> {

    List<UserChannelJpa> findAllByIdAccountOneOrIdAccountTwo(AccountJpa accountOne, AccountJpa accountTwo);

    boolean existsByIdChannelId(Long channelId);

    Optional<UserChannelJpa> findByIdChannelId(Long channelId);

    boolean existsByIdAccountOneOrIdAccountTwo(AccountJpa accountOne, AccountJpa accountTwo);
}
