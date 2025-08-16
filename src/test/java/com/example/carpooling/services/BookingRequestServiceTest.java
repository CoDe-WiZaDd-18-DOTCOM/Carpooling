package com.example.carpooling.services;

import com.example.carpooling.dto.BookingWrapper;
import com.example.carpooling.dto.SearchRequest;
import com.example.carpooling.entities.BookingRequest;
import com.example.carpooling.entities.Location;
import com.example.carpooling.entities.Ride;
import com.example.carpooling.entities.User;
import com.example.carpooling.enums.RideStatus;
import com.example.carpooling.repositories.BookingRequestRepository;
import com.example.carpooling.repositories.RideRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingRequestServiceTest {

    @Mock
    private BookingRequestRepository bookingRequestRepository;

    @Mock
    private RideRepository rideRepository;

    @Mock
    private RedisService redisService;

    @Mock
    private AnalyticsService analyticsService;

    @InjectMocks
    private BookingRequestService bookingRequestService;

    private User driver;
    private User rider;
    private Ride ride;
    private BookingRequest bookingRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        driver = new User();
        driver.setId(new ObjectId());
        driver.setFirstName("Driver");

        rider = new User();
        rider.setId(new ObjectId());
        rider.setFirstName("Rider");

        ride = new Ride();
        ride.setId(new ObjectId());
        ride.setDriver(driver);
        ride.setAvailableSeats(2);
        ride.setSeatCapacity(2);
        ride.setStatus(RideStatus.OPEN);

        bookingRequest = new BookingRequest();
        bookingRequest.setId(new ObjectId());
        bookingRequest.setRide(ride);
        bookingRequest.setDriver(driver);
        bookingRequest.setRider(rider);
        bookingRequest.setApproved(false);
        bookingRequest.setRated(false);
    }

    @Test
    void getBooking_ShouldReturnBooking_WhenExists() {
        when(bookingRequestRepository.findById(bookingRequest.getId())).thenReturn(Optional.of(bookingRequest));

        BookingRequest result = bookingRequestService.getBooking(bookingRequest.getId());

        assertEquals(bookingRequest.getId(), result.getId());
    }

    @Test
    void getBooking_ShouldReturnNull_WhenNotExists() {
        when(bookingRequestRepository.findById(any())).thenReturn(Optional.empty());

        BookingRequest result = bookingRequestService.getBooking(new ObjectId());

        assertNull(result);
    }

    @Test
    void getIncomingRequestsForDriver_ShouldReturnWrappedList() {
        when(bookingRequestRepository.findAllByDriver(driver)).thenReturn(List.of(bookingRequest));

        List<BookingWrapper> wrappers = bookingRequestService.getIncomingRequestsForDriver(driver);

        assertEquals(1, wrappers.size());
        assertEquals(driver.getId(), wrappers.get(0).getBookingRequest().getDriver().getId());
    }

    @Test
    void addRequest_ShouldSetDefaultsAndSave() {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.setDrop(new Location("xx",1.0,1.0));
        searchRequest.setPickup(new Location("xx",1.0,1.0));

        when(bookingRequestRepository.save(any(BookingRequest.class))).thenAnswer(i -> i.getArguments()[0]);

        BookingRequest result = bookingRequestService.addRequest(searchRequest, ride, rider);

        assertFalse(result.isApproved());
        assertFalse(result.isRated());
        assertEquals(rider, result.getRider());
        assertEquals(ride, result.getRide());
        assertEquals(driver, result.getDriver());
        assertEquals("xx", result.getDestination().getLabel());
        assertEquals("xx", result.getPickup().getLabel());
    }

    @Test
    void approveRequest_ShouldApprove_WhenSeatsAvailable() {
        when(rideRepository.save(any(Ride.class))).thenAnswer(i -> i.getArguments()[0]);
        when(bookingRequestRepository.save(any(BookingRequest.class))).thenAnswer(i -> i.getArguments()[0]);

        BookingRequest approved = bookingRequestService.approveRequest(bookingRequest);

        assertTrue(approved.isApproved());
        assertEquals(1, approved.getRide().getAvailableSeats());
        verify(analyticsService, times(1)).incBookings();
    }

    @Test
    void approveRequest_ShouldNotApprove_WhenNoSeats() {
        ride.setAvailableSeats(0);

        BookingRequest result = bookingRequestService.approveRequest(bookingRequest);

        assertNull(result);
        assertEquals(RideStatus.FILLED, ride.getStatus());
        verify(redisService, times(1)).delete("rides");
    }

    @Test
    void getBookingByRide_ShouldReturnWrappedList() {
        when(bookingRequestRepository.findAllByRide(ride)).thenReturn(List.of(bookingRequest));

        List<BookingWrapper> result = bookingRequestService.getBookingByRide(ride);

        assertEquals(1, result.size());
        assertEquals(ride.getId(), result.get(0).getBookingRequest().getRide().getId());
    }

    @Test
    void deleteRequest_ShouldCallRepository() {
        doNothing().when(bookingRequestRepository).deleteById(bookingRequest.getId());

        bookingRequestService.deleteRequest(bookingRequest.getId());

        verify(bookingRequestRepository, times(1)).deleteById(bookingRequest.getId());
    }
}
