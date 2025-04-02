package com.group11.accord.app.services;

import com.group11.accord.api.user.Account;
import com.group11.accord.api.user.NewAccount;
import com.group11.accord.api.user.SessionCredentials;
import com.group11.accord.app.exceptions.ErrorMessages;
import com.group11.accord.app.exceptions.InvalidCredentialsException;
import com.group11.accord.jpa.user.AccountJpa;
import com.group11.accord.jpa.user.AccountRepository;
import com.group11.accord.jpa.user.session.SessionJpa;
import com.group11.accord.jpa.user.session.SessionRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.Base64;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthorizationService {

    private final AccountRepository accountRepository;
    private final SessionRepository sessionRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    //Endpoint services
    public Account createAccount(@NonNull NewAccount newAccount){

        if(accountRepository.existsByUsername(newAccount.username())){
            throw new EntityExistsException("An Account with the given username already exists");
        }

        String salt = generateSalt();
        String hashedPassword = hashPassword(newAccount.password(), salt);

        return accountRepository.save(AccountJpa.create(newAccount.username(), hashedPassword, salt)).toDto();
    }

    public SessionCredentials loginAccount(String username, String password){
        AccountJpa accountJpa = accountRepository.findByUsername(username)
                //If there is no account without the given username we respond with an error
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_USERNAME.formatted(username)));

        validatePassword(accountJpa, password);

        return sessionRepository.save(SessionJpa.create(generateToken(), accountJpa)).getId().toDto();
    }

    public void logoutAccount(Long accountId, String token) {
        validateSession(accountId, token);

        sessionRepository.deleteByIdAccountIdAndIdToken(accountId, token);
    }

    public void changePassword(Long accountId, String token, String newPassword, String oldPassword) {
        AccountJpa accountJpa = accountRepository.findById(accountId)
                .orElseThrow(() -> new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(accountId)));

        validateSession(accountId, token);
        validatePassword(accountJpa, oldPassword);

        String salt = generateSalt();

        //Set new password with new salt
        accountJpa.setPassword(hashPassword(newPassword, salt));
        accountJpa.setSalt(salt);

        accountRepository.save(accountJpa);
    }

    public AccountJpa findValidAccount(Long accountId, String token) {
        validateSession(accountId, token);

        return accountRepository.findById(accountId)
                .orElseThrow(()->new EntityNotFoundException(ErrorMessages.MISSING_ACCOUNT_WITH_ID.formatted(accountId)));
    }

    // Utility functions--written by William P
    public void validateSession(Long accountId, String token) {
        if (!sessionRepository.sessionExists(accountId, token)) {
            throw new InvalidCredentialsException(ErrorMessages.INVALID_CREDENTIALS);
        }
    }

    public void validatePassword(@NonNull AccountJpa accountJpa, String password) {
        boolean passwordMatches = passwordEncoder.matches(password + accountJpa.getSalt(), accountJpa.getPassword());
        if (!passwordMatches) {
            throw new InvalidCredentialsException(ErrorMessages.INVALID_CREDENTIALS);
        }
    }

    private String generateSalt() {
        final SecureRandom random = new SecureRandom();
        byte[] salt = new byte[32];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    private String generateToken() {
        final SecureRandom random = new SecureRandom();
        byte[] token = new byte[64];
        random.nextBytes(token);
        return Base64.getEncoder().encodeToString(token);
    }

    private String hashPassword(String password, String salt) {
        return passwordEncoder.encode(password + salt);
    }
}
