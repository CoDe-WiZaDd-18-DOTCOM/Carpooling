package com.example.carpooling.controller;

import com.example.carpooling.dto.SosAuthorityMapper;
import com.example.carpooling.entities.*;
import com.example.carpooling.services.*;
import com.example.carpooling.utils.AuthUtil;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class SosController {

    @Autowired
    private EmailService emailService;

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

    @PostMapping("/sos/alert/{id}")
    public ResponseEntity<String> sendSos(@PathVariable String id, @RequestBody String message) {
        try {
            ObjectId newid = new ObjectId(id);
            User rider = userService.getUser(authUtil.getEmail());
            BookingRequest bookingRequest = bookingRequestService.getBooking(newid);
            Ride ride = bookingRequest.getRide();
            User driver = ride.getDriver();

            sosAlertsService.addAlert(rider, message, bookingRequest);


            String area = bookingRequest.getPickup().getArea();
            String city = bookingRequest.getPickup().getCity();
            String authorityEmail = sosAuthoritiesService.getEmail(area, city);

            String subject = "🚨 SOS Alert from CarpoolConnect!";
            String body = """
                🚨 *Emergency SOS Alert from CarpoolConnect* 🚨

                📍 Ride Information:
                - Pickup: %s (%s)
                - Drop: %s (%s)
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
                    bookingRequest.getPickup().getArea(), bookingRequest.getPickup().getCity(),
                    bookingRequest.getDestination().getArea(), bookingRequest.getDestination().getCity(),
                    ride.getRoute().getFirst().getArrivalTime().toString(),
                    rider.getFirstName(), rider.getLastName(), rider.getEmail(), rider.getPhoneNumber(),
                    driver.getFirstName(), driver.getLastName(), driver.getEmail(), driver.getPhoneNumber(),
                    ride.getVehicle().getBrand(), ride.getVehicle().getModel(),
                    ride.getVehicle().getColor(), ride.getVehicle().getLicensePlate(),
                    message
            );

            emailService.sendEmergencyEmail(authorityEmail, subject, body);

            return ResponseEntity.ok("🚨 SOS alert sent successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to send SOS: " + e.getMessage());
        }
    }

    @PostMapping("/sos/authority")
    public ResponseEntity<SosAuthorities> postAuthority(@RequestBody SosAuthorityMapper sosAuthorityMapper){
        try{
            SosAuthorities sosAuthorities=sosAuthoritiesService.addAuthority(sosAuthorityMapper);
            return new ResponseEntity<>(sosAuthorities,HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        }
    }

    @GetMapping("/sos/alerts")
    public ResponseEntity<List<SosAlerts>> getAlerts(){
        try{
            System.out.println("hello");
            return new ResponseEntity<>(sosAlertsService.getAlerts(),HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/share-location/{id}")
    public ResponseEntity<String> shareLocation(@PathVariable ObjectId id, @RequestBody String location) {
        try {
            User user = userService.getUser(authUtil.getEmail());
            BookingRequest bookingRequest = bookingRequestService.getBooking(id);


            String to = user.getEmergencyEmail();

            String subject = "📍 Live Location Shared from CarpoolConnect";
            String body = "User " + user.getFirstName() + " " + user.getLastName() +
                    " has shared their current location during a ride.\n\nLocation: " + location +
                    "\n\nin the following ride" + bookingRequest.getPickup().getArea() +
                    " -------> " + bookingRequest.getDestination().getArea();

            emailService.sendEmergencyEmail(to, subject, body);
            return ResponseEntity.ok("Location shared successfully!");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to share location: " + e.getMessage());
        }
    }

}

