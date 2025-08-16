package com.example.carpooling.services;

import com.example.carpooling.dto.SosAlertWrapper;
import com.example.carpooling.entities.BookingRequest;
import com.example.carpooling.entities.SosAlerts;
import com.example.carpooling.enums.SosStatus;
import com.example.carpooling.repositories.SosAlertsRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SosAlertsServiceTest {

    @Mock
    private SosAlertsRepository sosAlertsRepository;

    @Mock
    private RedisService redisService;

    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private SosAlertsService sosAlertsService;

    private BookingRequest bookingRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        bookingRequest = new BookingRequest();
        bookingRequest.setId(new ObjectId());
    }

    @Test
    void addAlert_ShouldSaveAlert_IncrementAnalytics_AndDeleteCache() {
        String message = "Emergency!";
        SosAlerts savedAlert = new SosAlerts(message, bookingRequest);
        when(sosAlertsRepository.save(any(SosAlerts.class))).thenReturn(savedAlert);

        SosAlerts result = sosAlertsService.addAlert(message, bookingRequest);

        assertEquals(message, result.getMessage());
        verify(sosAlertsRepository, times(1)).save(any(SosAlerts.class));
        verify(analyticsService, times(1)).incSos();
        verify(redisService, times(1)).delete("sosAlerts");
    }

    @Test
    void getAlerts_ShouldReturnFromCache_WhenCached() {
        List<SosAlertWrapper> cachedList = new ArrayList<>();
        SosAlerts sosAlerts=new SosAlerts("Msg1", bookingRequest);
        sosAlerts.setId(new ObjectId());
        cachedList.add(new SosAlertWrapper(sosAlerts));

        when(redisService.getList("sosAlerts", SosAlertWrapper.class)).thenReturn(cachedList);

        List<SosAlertWrapper> result = sosAlertsService.getAlerts();

        assertEquals(1, result.size());
        verify(redisService, times(1)).getList("sosAlerts", SosAlertWrapper.class);
        verify(sosAlertsRepository, never()).findAll();
    }

    @Test
    void getAlerts_ShouldFetchFromRepo_AndCache_WhenNotCached() {
        when(redisService.getList("sosAlerts", SosAlertWrapper.class)).thenReturn(null);

        List<SosAlerts> alertsFromRepo = new ArrayList<>();
        SosAlerts sosAlerts=new SosAlerts("Msg1", bookingRequest);
        sosAlerts.setId(new ObjectId());
        alertsFromRepo.add(sosAlerts);
        when(sosAlertsRepository.findAll()).thenReturn(alertsFromRepo);

        List<SosAlertWrapper> result = sosAlertsService.getAlerts();

        assertEquals(1, result.size());
        verify(redisService, times(1)).set(eq("sosAlerts"), anyList(), eq(3*60*60L));
        verify(sosAlertsRepository, times(1)).findAll();
    }

    @Test
    void closeAlert_ShouldMarkAlertAsSolved_WhenAlertExists() {
        SosAlerts alert = new SosAlerts("Msg1", bookingRequest);
        ObjectId alertId = new ObjectId();
        alert.setId(alertId);

        when(sosAlertsRepository.findById(alertId)).thenReturn(Optional.of(alert));
        when(sosAlertsRepository.save(alert)).thenReturn(alert);

        sosAlertsService.closeAlert(alertId);

        assertEquals(SosStatus.SOLVED, alert.getStatus());
        verify(sosAlertsRepository, times(1)).findById(alertId);
        verify(sosAlertsRepository, times(1)).save(alert);
    }

    @Test
    void closeAlert_ShouldDoNothing_WhenAlertNotFound() {
        ObjectId alertId = new ObjectId();
        when(sosAlertsRepository.findById(alertId)).thenReturn(Optional.empty());

        sosAlertsService.closeAlert(alertId);

        verify(sosAlertsRepository, times(1)).findById(alertId);
        verify(sosAlertsRepository, never()).save(any());
    }
}
