package com.example.carpooling.services;

import com.example.carpooling.dto.RideDto;
import com.example.carpooling.dto.SearchRequest;
import com.example.carpooling.dto.SearchResponse;
import com.example.carpooling.dto.UpdateRideDto;
import com.example.carpooling.entities.*;
import com.example.carpooling.enums.GenderPreference;
import com.example.carpooling.enums.PreferenceOption;
import com.example.carpooling.enums.RideStatus;
import com.example.carpooling.utils.RouteComparisonUtil;
import com.example.carpooling.repositories.BookingRequestRepository;
import com.example.carpooling.repositories.RideRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RideServiceTest {

    @Mock
    private RideRepository rideRepository;

    @Mock
    private BookingRequestRepository bookingRequestRepository;

    @Mock
    private RouteComparisonUtil routeComparisonUtil;

    @Mock
    private RedisService redisService;

    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private RideService rideService;

    private User driver;
    private Ride sampleRide;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        driver = new User();
        driver.setId(new ObjectId());
        driver.setFirstName("Driver");

        sampleRide = new Ride();
        sampleRide.setId(new ObjectId());
        sampleRide.setDriver(driver);
        sampleRide.setSeatCapacity(4);
        sampleRide.setAvailableSeats(4);
        sampleRide.setStatus(RideStatus.OPEN);
    }

    @Test
    void getAllRides_ShouldReturnRideWrappers() {
        when(rideRepository.findAll()).thenReturn(List.of(sampleRide));

        var result = rideService.getAllRides();

        assertEquals(1, result.size());
        assertEquals(driver.getId(), result.get(0).getRide().getDriver().getId());
        verify(rideRepository, times(1)).findAll();
    }

    @Test
    void getRide_ShouldReturnRide_WhenExists() {
        when(rideRepository.findById(sampleRide.getId())).thenReturn(Optional.of(sampleRide));

        Ride ride = rideService.getRide(sampleRide.getId());

        assertNotNull(ride);
        assertEquals(driver.getId(), ride.getDriver().getId());
    }

    @Test
    void getRide_ShouldReturnNull_WhenNotExists() {
        when(rideRepository.findById(any())).thenReturn(Optional.empty());

        Ride ride = rideService.getRide(new ObjectId());

        assertNull(ride);
    }

    @Test
    void addRide_ShouldSaveRide_AndIncrementAnalytics() {
        RideDto rideDto = new RideDto();
        rideDto.setRoute(List.of());
        rideDto.setSeatCapacity(4);
        rideDto.setAvailableSeats(4);
        rideDto.setVehicle(new Vehicle("xx","xx","xx","xx"));
        rideDto.setPreferences(new Preferences(PreferenceOption.NO,PreferenceOption.NO,PreferenceOption.NO,PreferenceOption.NO, GenderPreference.FEMALE_ONLY));
        rideDto.setCity("CityA");

        when(rideRepository.save(any(Ride.class))).thenReturn(sampleRide);

        Ride ride = rideService.addRide(rideDto, driver);

        assertNotNull(ride);
        verify(rideRepository, times(1)).save(any(Ride.class));
        verify(analyticsService, times(1)).incRides(driver.getId().toHexString(), "CityA");
    }

    @Test
    void closeRide_ShouldSetStatusAndCompletedAt_WhenRideExists() {
        when(rideRepository.findById(sampleRide.getId())).thenReturn(Optional.of(sampleRide));
        when(rideRepository.save(sampleRide)).thenReturn(sampleRide);

        Ride closedRide = rideService.closeRide(sampleRide.getId());

        assertEquals(RideStatus.CLOSED, closedRide.getStatus());
        assertNotNull(closedRide.getCompletedAt());
        verify(rideRepository, times(1)).save(sampleRide);
    }

    @Test
    void updateRide_ShouldUpdateFields_WhenRideExists() {
        UpdateRideDto dto = new UpdateRideDto();
        dto.setSeatCapacity(6);
        dto.setAvailableSeats(6);
        dto.setRoute(List.of());
        dto.setVehicle(new Vehicle("xx","xx","xx","xx"));
        dto.setPreferences(new Preferences(PreferenceOption.NO,PreferenceOption.NO,PreferenceOption.NO,PreferenceOption.NO, GenderPreference.FEMALE_ONLY));
        dto.setVersion(2l);

        when(rideRepository.findById(sampleRide.getId())).thenReturn(Optional.of(sampleRide));
        when(rideRepository.save(sampleRide)).thenReturn(sampleRide);

        Ride updatedRide = rideService.updateRide(sampleRide.getId().toHexString(), dto);

        assertEquals(6, updatedRide.getSeatCapacity());
        assertEquals("xx", updatedRide.getVehicle().getBrand());
        verify(rideRepository, times(1)).save(sampleRide);
    }

    @Test
    void checkAndGetRequest_ShouldReturnBooking_WhenExists() {
        BookingRequest bookingRequest = new BookingRequest();
        when(bookingRequestRepository.existsByRideAndRider(sampleRide, driver)).thenReturn(true);
        when(bookingRequestRepository.findByRideAndRider(sampleRide, driver)).thenReturn(bookingRequest);

        BookingRequest result = rideService.checkAndGetRequest(sampleRide, driver);

        assertNotNull(result);
    }

    @Test
    void checkAndGetRequest_ShouldReturnNull_WhenNotExists() {
        when(bookingRequestRepository.existsByRideAndRider(sampleRide, driver)).thenReturn(false);

        BookingRequest result = rideService.checkAndGetRequest(sampleRide, driver);

        assertNull(result);
    }

}
