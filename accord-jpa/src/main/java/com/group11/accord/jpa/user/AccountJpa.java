package com.group11.accord.jpa.user;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

import java.io.Serializable;

// TODO: placeholder for Connor
@Getter
@Entity
public class AccountJpa implements Serializable {

    @Id
    Long id;
}
