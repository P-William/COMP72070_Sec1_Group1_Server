package com.group11.accord.app;

import com.group11.accord.api.user.Account;
import com.group11.accord.api.user.NewAccount;
import com.group11.accord.api.user.SessionCredentials;
import com.group11.accord.app.services.AuthorizationService;
import com.group11.accord.jpa.user.AccountJpa;
import com.group11.accord.jpa.user.AccountRepository;
import com.group11.accord.jpa.user.session.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthTests {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthorizationService authorizationService;

    @Test
    void createAccount_Success() {
        NewAccount newAccount = new NewAccount("Dan", "12345", "A cool guy");
        AccountJpa accountJpa = AccountJpa.builder()
                .id(1L)
                .username(newAccount.username())
                .password(newAccount.password())
                .salt("salt")
                .createdAt(LocalDateTime.now())
                .build();

        when(accountRepository.existsByUsername(newAccount.username())).thenReturn(false);

        when(accountRepository.save(any(AccountJpa.class))).thenReturn(accountJpa);

        Account account = authorizationService.createAccount(newAccount);

        assertNotNull(account);
        verify(accountRepository).save(any(AccountJpa.class));

    }

    @Test
    void loginAccount_Success() {
        String username = "Dan";
        String password = "12345";
        AccountJpa accountJpa = mock(AccountJpa.class);

        when(accountRepository.findByUsername(username)).thenReturn(Optional.of(accountJpa));
        when(accountJpa.getPassword()).thenReturn(new BCryptPasswordEncoder().encode(password + "salt"));
        when(accountJpa.getSalt()).thenReturn("salt");
        when(sessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        SessionCredentials sessionCredentials = authorizationService.loginAccount(username, password);

        assertNotNull(sessionCredentials);
        verify(sessionRepository).save(any());
    }

    @Test
    void logoutAccount_Success() {
        Long accountId = 1L;
        String token = "validToken";
        when(sessionRepository.sessionExists(accountId, token)).thenReturn(true);

        authorizationService.logoutAccount(accountId, token);

        verify(sessionRepository).deleteByIdAccountId(any());
    }

    @Test
    void changePassword_Success() {
        Long accountId = 1L;
        String token = "validToken";

        AccountJpa accountJpa = mock(AccountJpa.class);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(accountJpa));
        when(sessionRepository.sessionExists(accountId, token)).thenReturn(true);
        when(accountJpa.getPassword()).thenReturn(new BCryptPasswordEncoder().encode("oldPassword" + "salt"));
        when(accountJpa.getSalt()).thenReturn("salt");

        authorizationService.changePassword(accountId, token, "newPassword", "oldPassword");

        verify(accountRepository).save(any(AccountJpa.class));
    }
}
