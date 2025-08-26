package com.example.carpooling.services;

import com.example.carpooling.dto.AnalyticsDto;
import com.example.carpooling.entities.Analytics;
import com.example.carpooling.entities.User;
import com.example.carpooling.repositories.AnalyticsRepository;
import com.example.carpooling.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AnalyticsServiceTest {

//    @Mock
//    private AnalyticsRepository analyticsRepository;
//
//    @Mock
//    private UserRepository userRepository;
//
//    @InjectMocks
//    private AnalyticsService analyticsService;
//
//    private Analytics analytics;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        analytics = new Analytics();
//        analytics.setTotalUsers(10);
//        analytics.setTotalRides(5);
//        analytics.setTotalBookings(3);
//        analytics.setTotalSOS(2);
//        analytics.setRidesPerDriver(new HashMap<>());
//        analytics.setRidesByCity(new HashMap<>());
//    }
//
//    @Test
//    void getAnalytics_ShouldReturnExistingAnalytics() {
//        when(analyticsRepository.findById("default")).thenReturn(Optional.of(analytics));
//
//        Analytics result = analyticsService.getAnalytics();
//
//        assertEquals(analytics, result);
//        verify(analyticsRepository, never()).save(any());
//    }
//
//    @Test
//    void getAnalytics_ShouldCreateNewAnalytics_WhenNotExists() {
//        when(analyticsRepository.findById("default")).thenReturn(Optional.empty());
//
//        Analytics result = analyticsService.getAnalytics();
//
//        assertNotNull(result);
//        verify(analyticsRepository, times(1)).save(any(Analytics.class));
//    }
//
//    @Test
//    void get_ShouldMapDriverIdsToEmails() {
//        analytics.getRidesPerDriver().put("driverId", 3L);
//        User user = new User();
//        user.setId(new ObjectId("aaaaaaaaaaaaaaaaaaaaaaaa"));
//        user.setEmail("driver@example.com");
//
//        when(analyticsRepository.findById("default")).thenReturn(Optional.of(analytics));
//        when(userRepository.findById(new ObjectId("aaaaaaaaaaaaaaaaaaaaaaaa"))).thenReturn(Optional.of(user));
//
//        AnalyticsDto dto = analyticsService.get();
//
//        assertEquals(1, dto.getRidesPerDriver().size());
//        assertEquals(3L, dto.getRidesPerDriver().get("driver@example.com"));
//    }
//
//    @Test
//    void incUsers_ShouldIncrementAndSave() {
//        when(analyticsRepository.findById("default")).thenReturn(Optional.of(analytics));
//
//        analyticsService.incUsers();
//
//        assertEquals(11, analytics.getTotalUsers());
//        verify(analyticsRepository, times(1)).save(analytics);
//    }
//
//    @Test
//    void incRides_ShouldIncrementTotalAndMaps() {
//        analytics.getRidesPerDriver().put("driver1", 2L);
//        analytics.getRidesByCity().put("CityA", 1L);
//
//        when(analyticsRepository.findById("default")).thenReturn(Optional.of(analytics));
//
//        analyticsService.incRides("driver1", "CityA");
//
//        assertEquals(6, analytics.getTotalRides());
//        assertEquals(3L, analytics.getRidesPerDriver().get("driver1"));
//        assertEquals(2L, analytics.getRidesByCity().get("CityA"));
//
//        verify(analyticsRepository, times(1)).save(analytics);
//    }
//
//    @Test
//    void incBookings_ShouldIncrementTotalBookings() {
//        when(analyticsRepository.findById("default")).thenReturn(Optional.of(analytics));
//
//        analyticsService.incBookings();
//
//        assertEquals(4, analytics.getTotalBookings());
//        verify(analyticsRepository, times(1)).save(analytics);
//    }
//
//    @Test
//    void incSos_ShouldIncrementTotalSOS() {
//        when(analyticsRepository.findById("default")).thenReturn(Optional.of(analytics));
//
//        analyticsService.incSos();
//
//        assertEquals(3, analytics.getTotalSOS());
//        verify(analyticsRepository, times(1)).save(analytics);
//    }
}
