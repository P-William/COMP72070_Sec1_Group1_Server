package com.group11.accord.jpa.server.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ServerBanRepository extends JpaRepository<ServerBanJpa, Long> {


}
