package com.group11.accord.app.services;

import com.group11.accord.api.user.Account;
import com.group11.accord.api.user.NewAccount;
import com.group11.accord.api.user.SessionCredentials;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorizationService {

    public Account createAccount(NewAccount newAccount){
        return null;
    }

    public SessionCredentials loginAccount(String username, String password){
        return null;
    }

    public void changePassword(Long accountId, String token, String newPassword, String oldPassword) {

    }
}
