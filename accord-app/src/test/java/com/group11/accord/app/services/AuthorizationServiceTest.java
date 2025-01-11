package com.group11.accord.app.services;

import com.group11.accord.api.driver.Driver;
import com.group11.accord.api.driver.DriverUpload;
import com.group11.accord.api.driver.Session;
import com.group11.accord.app.exceptions.InvalidCredentialsException;
import com.group11.accord.jpa.driver.DriverJpa;
import com.group11.accord.jpa.driver.DriverRepository;
import com.group11.accord.jpa.driver.session.SessionRepository;
import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {
    @Mock
    private DriverRepository driverRepository;

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

    @Test
    void createDriver_Success() {
        DriverUpload driverUpload = new DriverUpload("John", "Doe", "johndoe", "password123");
        DriverJpa driverJpa = DriverJpa.builder()
            .id(1L)
            .firstName(driverUpload.firstName())
            .lastName(driverUpload.lastName())
            .username(driverUpload.username())
            .password(driverUpload.password())
            .salt("salt")
            .createdAt(LocalDateTime.now())
            .build();
        when(driverRepository.existsByUsername(driverUpload.username()))
            .thenReturn(false);
        when(driverRepository.save(any(DriverJpa.class)))
            .thenReturn(driverJpa);

        Driver driver = authorizationService.createDriver(driverUpload);

        assertNotNull(driver);
        verify(driverRepository).save(any(DriverJpa.class));
    }

    @Test
    void createDriver_UsernameExists() {
        DriverUpload driverUpload = new DriverUpload("John", "Doe", "johndoe", "password123");
        when(driverRepository.existsByUsername(driverUpload.username()))
            .thenReturn(true);

        assertThrows(EntityExistsException.class, () -> authorizationService.createDriver(driverUpload));
    }

    @Test
    void loginDriver_Success() {
        String username = "johndoe";
        String password = "password123";
        DriverJpa driverJpa = mock(DriverJpa.class);
        when(driverRepository.findByUsername(username)).thenReturn(Optional.of(driverJpa));
        when(driverJpa.getPassword()).thenReturn(new BCryptPasswordEncoder().encode(password + "salt"));
        when(driverJpa.getSalt()).thenReturn("salt");
        when(sessionRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Session session = authorizationService.loginDriver(username, password);

        assertNotNull(session);
        verify(sessionRepository).save(any());
    }

    @Test
    void loginDriver_InvalidCredentials() {
        String username = "johndoe";
        String password = "wrongpassword";
        DriverJpa driverJpa = mock(DriverJpa.class);
        when(driverRepository.findByUsername(username)).thenReturn(Optional.of(driverJpa));
        when(driverJpa.getPassword()).thenReturn(new BCryptPasswordEncoder().encode("password123" + "salt"));
        when(driverJpa.getSalt()).thenReturn("salt");

        assertThrows(InvalidCredentialsException.class, () -> authorizationService.loginDriver(username, password));
    }

    @Test
    void changePassword_Success() {
        Long driverId = 1L;
        String token = "validToken";
        String oldPassword = "oldPassword";
        String newPassword = "newPassword";
        DriverJpa driverJpa = mock(DriverJpa.class);
        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driverJpa));
        when(driverJpa.getPassword()).thenReturn(new BCryptPasswordEncoder().encode(oldPassword + "salt"));
        when(driverJpa.getSalt()).thenReturn("salt");
        when(sessionRepository.sessionExists(driverId, token)).thenReturn(true);

        authorizationService.changePassword(driverId, token, oldPassword, newPassword);

        verify(driverRepository).save(driverJpa);
    }

    @Test
    void changePassword_InvalidOldPassword() {
        Long driverId = 1L;
        String token = "validToken";
        String oldPassword = "wrongOldPassword";
        String newPassword = "newPassword";
        DriverJpa driverJpa = mock(DriverJpa.class);
        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driverJpa));
        when(driverJpa.getPassword()).thenReturn(new BCryptPasswordEncoder().encode("oldPassword" + "salt"));
        when(driverJpa.getSalt()).thenReturn("salt");
        when(sessionRepository.sessionExists(driverId, token)).thenReturn(true);

        assertThrows(InvalidCredentialsException.class, () -> authorizationService.changePassword(driverId, token, oldPassword, newPassword));
    }

    @Test
    void removeAllSessions_Success() {
        Long driverId = 1L;

        authorizationService.removeAllSessions(driverId);

        verify(sessionRepository).deleteAllByIdDriverId(driverId);
    }

    @Test
    void logoutDriver_Success() {
        Long driverId = 1L;
        String token = "validToken";
        when(sessionRepository.sessionExists(driverId, token)).thenReturn(true);

        authorizationService.logoutDriver(driverId, token);

        verify(sessionRepository).deleteById(any());
    }

    @Test
    void logoutDriver_InvalidSession() {
        Long driverId = 1L;
        String token = "invalidToken";
        when(sessionRepository.sessionExists(driverId, token)).thenReturn(false);

        assertThrows(InvalidCredentialsException.class, () -> authorizationService.logoutDriver(driverId, token));
    }
}
