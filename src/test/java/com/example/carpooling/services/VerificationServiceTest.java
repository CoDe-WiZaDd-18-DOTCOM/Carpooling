package com.example.carpooling.services;

import com.example.carpooling.dto.EmailDto;
import com.example.carpooling.queues.producers.EmailProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

class VerificationServiceTest {

    @Mock
    private RedisService redisService;

    @Mock
    private EmailProducer emailProducer;

    @InjectMocks
    private VerificationService verificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendOtp_ShouldStoreOtpInRedis_AndSendEmail() {
        String email = "test@example.com";

        verificationService.sendOtp(email);

        verify(redisService, times(1)).set(eq(email), anyString(), eq(120L));
        verify(emailProducer, times(1)).sendEmail(any(EmailDto.class));


    }

    @Test
    void validateOtp_ShouldReturnTrue_WhenOtpMatches() {
        String email = "test@example.com";
        String otp = "123456";

        when(redisService.get(email, String.class)).thenReturn("123456");

        boolean result = verificationService.validateOtp(email, otp);

        assertTrue(result);
        verify(redisService, times(1)).get(email, String.class);
    }

    @Test
    void validateOtp_ShouldReturnFalse_WhenOtpDoesNotMatch() {
        String email = "test@example.com";
        String otp = "111111";

        when(redisService.get(email, String.class)).thenReturn("123456");

        boolean result = verificationService.validateOtp(email, otp);

        assertFalse(result);
        verify(redisService, times(1)).get(email, String.class);
    }

    @Test
    void validateOtp_ShouldReturnFalse_WhenOtpIsNull() {
        String email = "test@example.com";
        String otp = "123456";

        when(redisService.get(email, String.class)).thenReturn(null);

        boolean result = verificationService.validateOtp(email, otp);

        assertFalse(result);
        verify(redisService, times(1)).get(email, String.class);
    }
}
