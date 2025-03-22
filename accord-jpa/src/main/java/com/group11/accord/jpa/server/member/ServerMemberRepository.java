package com.group11.accord.jpa.server.member;

import com.group11.accord.api.server.Server;
import com.group11.accord.jpa.server.ServerJpa;
import com.group11.accord.jpa.user.AccountJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerMemberRepository extends JpaRepository<ServerMemberJpa, Long> {

    boolean existsByIdAccount(AccountJpa accountJpa);
    boolean existsByIdAccountIdAndIdServerId(Long accountId, Long serverId);


    List<ServerMemberJpa> findAllByIdServer(ServerJpa serverJpa);

    void deleteByIdAccountIdAndIdServerId(Long accountId, Long serverId);
}
