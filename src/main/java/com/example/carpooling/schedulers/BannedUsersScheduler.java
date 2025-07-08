package com.example.carpooling.schedulers;

import com.example.carpooling.entities.Banned;
import com.example.carpooling.entities.Ride;
import com.example.carpooling.services.BannedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BannedUsersScheduler {
    @Autowired
    private BannedService bannedService;

    private static final Logger logger = LoggerFactory.getLogger(BannedUsersScheduler.class);

    @Scheduled(cron = "0 0 2 * * ?")
    public void cleanup(){
        try {
            List<Banned> banneds = bannedService.findAll();
            for (Banned banned:banneds){
                LocalDateTime localDateTime = LocalDateTime.now().minusDays(banned.getDuration());
                if (banned.getLocalDateTime().isBefore(localDateTime)) bannedService.liftBan(banned.getEmail());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
