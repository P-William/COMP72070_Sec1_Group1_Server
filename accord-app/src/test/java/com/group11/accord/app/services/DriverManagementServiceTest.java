package com.group11.accord.app.services;

import com.group11.accord.api.driver.Driver;
import com.group11.accord.jpa.driver.DriverJpa;
import com.group11.accord.jpa.driver.DriverRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverManagementServiceTest {

    @Mock
    private DriverRepository driverRepository;
    @Mock
    private AuthorizationService authorizationService;
    @Mock
    private SchedulingService schedulingService;

    @InjectMocks
    private DriverManagementService driverManagementService;

    @Test
    void getDriverById_Success() {
        Long driverId = 1L;
        String token = "validToken";
        DriverJpa driverJpa = DriverJpa.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .username("johndoe")
            .password("password123")
            .salt("salt")
            .createdAt(LocalDateTime.now())
            .build();
        when(driverRepository.findById(driverId))
            .thenReturn(Optional.of(driverJpa));

        Driver driver = driverManagementService.getDriverById(driverId, token);

        assertThat(driver).isNotNull();

        verify(driverRepository).findById(driverId);
        verify(authorizationService).validateSession(driverId, token);
    }

    @Test
    void getDriverById_DriverNotFound() {
        Long driverId = 1L;
        String token = "validToken";
        when(driverRepository.findById(driverId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> driverManagementService.getDriverById(driverId, token));
    }

    @Test
    void updateName_Success() {
        Long driverId = 1L;
        String token = "validToken";
        String firstName = "John";
        String lastName = "Doe";
        DriverJpa driverJpa = DriverJpa.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .username("johndoe")
            .password("password123")
            .salt("salt")
            .createdAt(LocalDateTime.now())
            .build();
        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driverJpa));
        doNothing().when(authorizationService).validateSession(driverId, token);
        when(driverRepository.save(driverJpa)).thenReturn(driverJpa);

        Driver driver = driverManagementService.updateName(driverId, token, firstName, lastName);

        assertThat(driver).isNotNull();
        verify(driverRepository).findById(driverId);
        verify(authorizationService).validateSession(driverId, token);
        verify(driverRepository).save(driverJpa);
    }

    @Test
    void updateUsername_Success() {
        Long driverId = 1L;
        String token = "validToken";
        String username = "newUsername";
        DriverJpa driverJpa = DriverJpa.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .username("johndoe")
            .password("password123")
            .salt("salt")
            .createdAt(LocalDateTime.now())
            .build();
        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driverJpa));
        doNothing().when(authorizationService).validateSession(driverId, token);
        when(driverRepository.existsByUsername(username)).thenReturn(false);
        when(driverRepository.save(driverJpa)).thenReturn(driverJpa);

        Driver driver = driverManagementService.updateUsername(driverId, token, username);

        assertThat(driver).isNotNull();
        verify(driverRepository).findById(driverId);
        verify(authorizationService).validateSession(driverId, token);
        verify(driverRepository).existsByUsername(username);
        verify(driverRepository).save(driverJpa);
    }

    @Test
    void updateUsername_UsernameExists() {
        Long driverId = 1L;
        String token = "validToken";
        String username = "existingUsername";
        DriverJpa driverJpa = mock(DriverJpa.class);
        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driverJpa));
        doNothing().when(authorizationService).validateSession(driverId, token);
        when(driverRepository.existsByUsername(username)).thenReturn(true);

        assertThrows(EntityExistsException.class, () -> driverManagementService.updateUsername(driverId, token, username));
    }

    @Test
    void deleteDriver_Success() {
        Long driverId = 1L;
        String token = "validToken";
        String password = "password123";
        DriverJpa driverJpa = mock(DriverJpa.class);
        when(driverRepository.findById(driverId)).thenReturn(Optional.of(driverJpa));
        doNothing().when(authorizationService).validateSession(driverId, token);
        doNothing().when(authorizationService).validatePassword(driverJpa, password);
        doNothing().when(schedulingService).scheduleAccountForDeletion(driverId);
        doNothing().when(authorizationService).removeAllSessions(driverId);
        when(driverRepository.save(driverJpa)).thenReturn(driverJpa);

        driverManagementService.deleteDriver(driverId, token, password);

        verify(driverRepository).findById(driverId);
        verify(authorizationService).validateSession(driverId, token);
        verify(authorizationService).validatePassword(driverJpa, password);
        verify(schedulingService).scheduleAccountForDeletion(driverId);
        verify(authorizationService).removeAllSessions(driverId);
        verify(driverRepository).save(driverJpa);
    }

    @Test
    void recoverDriver_Success() {
        String username = "deletedUser";
        String password = "password123";
        DriverJpa driverJpa = DriverJpa.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .username("johndoe")
            .password("password123")
            .salt("salt")
            .createdAt(LocalDateTime.now())
            .deleted(true)
            .build();
        when(driverRepository.findByUsernameIncludingDeleted(username)).thenReturn(Optional.of(driverJpa));
        doNothing().when(authorizationService).validatePassword(driverJpa, password);
        doNothing().when(schedulingService).removeAccountDeletionTrigger(driverJpa.getId());
        when(driverRepository.save(driverJpa)).thenReturn(driverJpa);

        Driver driver = driverManagementService.recoverDriver(username, password);

        assertThat(driver).isNotNull();
        verify(driverRepository).findByUsernameIncludingDeleted(username);
        verify(authorizationService).validatePassword(driverJpa, password);
        verify(schedulingService).removeAccountDeletionTrigger(driverJpa.getId());
        verify(driverRepository).save(driverJpa);
    }

    @Test
    void recoverDriver_NotDeleted() {
        String username = "activeUser";
        String password = "password123";
        DriverJpa driverJpa = mock(DriverJpa.class);
        when(driverRepository.findByUsernameIncludingDeleted(username)).thenReturn(Optional.of(driverJpa));
        when(driverJpa.isDeleted()).thenReturn(false);

        assertThrows(IllegalStateException.class, () -> driverManagementService.recoverDriver(username, password));
    }
}