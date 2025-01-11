package com.group11.accord.app.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.quartz.*;
import org.springframework.scheduling.SchedulingException;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doThrow;

@ExtendWith(MockitoExtension.class)
class SchedulingServiceTest {

    @Mock
    private Scheduler scheduler;

    @InjectMocks
    private SchedulingService schedulingService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(schedulingService, "accountRecoveryTime", 7);
    }

    @Test
    void scheduleAccountForDeletion_schedulesJobSuccessfully() throws SchedulerException {
        Long driverId = 1L;
        when(scheduler.checkExists(any(JobKey.class))).thenReturn(false);

        schedulingService.scheduleAccountForDeletion(driverId);

        verify(scheduler, times(1)).scheduleJob(any(Trigger.class));
    }

    @Test
    void scheduleAccountForDeletion_throwsSchedulingExceptionWhenSchedulerFails() throws SchedulerException {
        Long driverId = 1L;
        when(scheduler.checkExists(any(JobKey.class))).thenReturn(false);
        doThrow(SchedulerException.class).when(scheduler).scheduleJob(any(Trigger.class));

        assertThrows(SchedulingException.class, () -> schedulingService.scheduleAccountForDeletion(driverId));
    }

    @Test
    void removeAccountDeletionTrigger_removesTriggerSuccessfully() throws SchedulerException {
        Long driverId = 1L;

        schedulingService.removeAccountDeletionTrigger(driverId);

        verify(scheduler, times(1)).unscheduleJob(any(TriggerKey.class));
    }

    @Test
    void removeAccountDeletionTrigger_throwsSchedulingExceptionWhenSchedulerFails() throws SchedulerException {
        Long driverId = 1L;
        doThrow(SchedulerException.class).when(scheduler).unscheduleJob(any(TriggerKey.class));

        assertThrows(SchedulingException.class, () -> schedulingService.removeAccountDeletionTrigger(driverId));
    }
}