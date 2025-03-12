package com.group11.accord.jpa.server;

import com.group11.accord.jpa.user.AccountJpa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServerRepository extends JpaRepository<ServerJpa, Long> {

    boolean existsByOwnerIdAndId(Long ownerId, Long serverId);
    
    ServerJpa findByOwnerIdAndId(Long ownerId, Long serverId);

    List<ServerJpa> findAllByMembersIsContaining(AccountJpa accountJpa);

    //void deleteById(Long serverId);
}
