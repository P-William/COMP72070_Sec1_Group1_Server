package com.group11.accord.jpa.user;

import com.group11.accord.api.user.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<AccountJpa, Long> {

}
