package com.example.carpooling.controller;

import com.example.carpooling.dto.EmailDto;
import com.example.carpooling.dto.SosAlertWrapper;
import com.example.carpooling.dto.SosAuthorityMapper;
import com.example.carpooling.entities.*;
import com.example.carpooling.queues.producers.EmailProducer;
import com.example.carpooling.services.*;
import com.example.carpooling.utils.AuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Tag(name = "SOS", description = "APIs for emergency alerts, live location sharing, and SOS authority management.")
public class SosController {

    private static final Logger log = LoggerFactory.getLogger(SosController.class);
    @Autowired
    private EmailProducer emailProducer;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthUtil authUtil;

    @Autowired
    private BookingRequestService bookingRequestService;

    @Autowired
    private SosAlertsService sosAlertsService;

    @Autowired
    private SosAuthoritiesService sosAuthoritiesService;

    @Operation(
            summary = "Trigger an SOS alert",
            description = "Sends an SOS alert message containing booking, rider, and driver details to the relevant SOS authority via email."
    )
    @PostMapping("/sos/alert/{id}")
    public ResponseEntity<String> sendSos(@PathVariable String id, @RequestBody String message) {
        try {
            ObjectId newid = new ObjectId(id);
            BookingRequest bookingRequest = bookingRequestService.getBooking(newid);
            Ride ride = bookingRequest.getRide();
            User driver = ride.getDriver();
            User rider = bookingRequest.getRider();

            sosAlertsService.addAlert(message, bookingRequest);

            String label = bookingRequest.getPickup().getLabel();
            String authorityEmail = sosAuthoritiesService.getAuthority(label).getEmail();

            String subject = "🚨 SOS Alert from CarpoolConnect!";
            String body = """
                🚨 *Emergency SOS Alert from CarpoolConnect* 🚨

                📍 Ride Information:
                - Pickup: %s
                - Drop: %s
                - Date/Time: %s

                🧍‍♂️ Rider Information:
                - Name: %s %s
                - Email: %s
                - Phone: %s

                🚗 Driver Information:
                - Name: %s %s
                - Email: %s
                - Phone: %s
                - Vehicle: %s %s (%s, %s)

                🆘 User Message:
                "%s"

                Please respond immediately to this alert.
                """.formatted(
                    bookingRequest.getPickup().getLabel(),
                    bookingRequest.getDestination().getLabel(),
                    ride.getRoute().getFirst().getArrivalTime().toString(),
                    rider.getFirstName(), rider.getLastName(), rider.getEmail(), rider.getPhoneNumber(),
                    driver.getFirstName(), driver.getLastName(), driver.getEmail(), driver.getPhoneNumber(),
                    ride.getVehicle().getBrand(), ride.getVehicle().getModel(),
                    ride.getVehicle().getColor(), ride.getVehicle().getLicensePlate(),
                    message
            );

            emailProducer.sendEmail(new EmailDto(authorityEmail, subject, body));

            return ResponseEntity.ok("🚨 SOS alert sent successfully!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(500).body("Failed to send SOS: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Fetch all SOS alerts",
            description = "Returns all SOS alerts raised by users, including their messages and linked bookings."
    )
    @GetMapping("/sos/alerts")
    public ResponseEntity<List<SosAlertWrapper>> getAlerts() {
        try {
            return new ResponseEntity<>(sosAlertsService.getAlerts(), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }


    @Operation(
            summary = "Close a SOS alert",
            description = "Closes a SOS alert after it got solved."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/sos/close/{id}")
    public ResponseEntity<?> closeAlert(@PathVariable String id) {
        try {
            sosAlertsService.closeAlert(new ObjectId(id));
            return new ResponseEntity<>("closed", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }


    @Operation(
            summary = "Register an SOS authority",
            description = "Adds a new SOS authority (e.g. police or emergency unit) responsible for a specific area and city."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/sos/authority")
    public ResponseEntity<SosAuthorities> postAuthority(@RequestBody SosAuthorityMapper sosAuthorityMapper) {
        try {
            SosAuthorities sosAuthorities = sosAuthoritiesService.addAuthority(sosAuthorityMapper);
            return new ResponseEntity<>(sosAuthorities, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @Operation(
            summary = "get SOS authority",
            description = "gets the details of SOS authority for a specific area and city."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/sos/authority/{label}")
    public ResponseEntity<?> getAuthority(@PathVariable String label) {
        try {
            SosAuthorities sosAuthorities = sosAuthoritiesService.getAuthority(label);
            return new ResponseEntity<>(sosAuthorities, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "update SOS authority",
            description = "update the details of SOS authority for a specific area and city."
    )
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/sos/authority/{label}")
    public ResponseEntity<?> updateAuthority(@PathVariable String label,@RequestBody SosAuthorityMapper sosAuthorityMapper) {
        try {
            SosAuthorities sosAuthorities = sosAuthoritiesService.updateAuthority(label,sosAuthorityMapper);
            return new ResponseEntity<>(sosAuthorities, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(
            summary = "Share current location",
            description = "Allows a rider to send their live location during a ride to their emergency contact via email."
    )
    @PostMapping("/share-location/{id}")
    public ResponseEntity<String> shareLocation(@PathVariable ObjectId id, @RequestBody String location) {
        try {
            User user = userService.getUserById(authUtil.getId());
            BookingRequest bookingRequest = bookingRequestService.getBooking(id);

            String to = user.getEmergencyEmail();
            String subject = "📍 Live Location Shared from CarpoolConnect";
            String body = "User " + user.getFirstName() + " " + user.getLastName() +
                    " has shared their current location during a ride.\n\nLocation: " + location +
                    "\n\nin the following ride: " + bookingRequest.getPickup().getLabel() +
                    " -------> " + bookingRequest.getDestination().getLabel();

            emailProducer.sendEmail(new EmailDto(to, subject, body));
            return ResponseEntity.ok("Location shared successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to share location: " + e.getMessage());
        }
    }
}
