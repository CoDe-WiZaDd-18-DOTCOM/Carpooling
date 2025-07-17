package com.example.carpooling.controller;

import com.example.carpooling.dto.BannedDto;
import com.example.carpooling.entities.Banned;
import com.example.carpooling.entities.User;
import com.example.carpooling.enums.Role;
import com.example.carpooling.services.BannedService;
import com.example.carpooling.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ban")
@PreAuthorize("hasRole('ADMIN')")
@Tag(name = "Banned Users", description = "Endpoints for managing ban of users.")
public class BannedController {
    @Autowired
    private BannedService bannedService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(BannedController.class);

    @Operation(summary = "Get Banned list", description = "Get list of banned users")
    @GetMapping("/all")
    public ResponseEntity<?> getAll(){
        try {
            return new ResponseEntity<>(bannedService.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "find user", description = "find user by entering his email if not present then null will be returned.")
    @GetMapping("/user/{email}")
    public ResponseEntity<?> getUser(@PathVariable String email){
        try {
            return new ResponseEntity<>(bannedService.findByEmail(email), HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Ban user", description = "Ban user by providing all the required details")
    @PostMapping("/user")
    public ResponseEntity<?> banUser(@RequestBody BannedDto bannedDto){
        try {
            Banned banned = new Banned(bannedDto);
            logger.info("user banned: "+banned.getEmail());
            bannedService.banUser(banned);
            return new ResponseEntity<>(banned, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
            return new ResponseEntity<>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping("/user/{email}")
    public ResponseEntity<?> deleteUser(@PathVariable String email){
        try {
            logger.info("🔍 Checking if user is banned: " + email);
            if (!bannedService.existByEmail(email)) {
                logger.warn("⚠️ User not found in banned list: " + email);
                return new ResponseEntity<>("user doesn't exist", HttpStatus.BAD_REQUEST);
            }

            logger.info("✅ User is banned. Proceeding to lift ban for: " + email);
            bannedService.liftBan(email);
            logger.info("🚫 Ban lifted on user: " + email);
            return ResponseEntity.ok("Done");

        } catch (Exception e) {
            logger.error("🔥 Exception while lifting ban: ", e);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
