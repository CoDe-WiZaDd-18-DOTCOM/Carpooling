package com.example.carpooling.services;

import com.example.carpooling.dto.UserProfileDto;
import com.example.carpooling.entities.User;
import com.example.carpooling.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

//    @Mock
//    private UserRepository userRepository;
//
//    @Mock
//    private RedisService redisService;
//
//    @Mock
//    private AnalyticsService analyticsService;
//
//    @InjectMocks
//    private UserService userService;
//
//    private User sampleUser;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        sampleUser = new User();
//        sampleUser.setId(new ObjectId());
//        sampleUser.setFirstName("John");
//        sampleUser.setLastName("Doe");
//        sampleUser.setEmail("john@example.com");
//        sampleUser.setPhoneNumber("1234567890");
//    }
//
//    @Test
//    void getUserById_ShouldReturnUser_WhenExists() {
//        when(userRepository.findById(sampleUser.getId())).thenReturn(Optional.of(sampleUser));
//
//        User result = userService.getUserById(sampleUser.getId().toHexString());
//
//        assertNotNull(result);
//        assertEquals("John", result.getFirstName());
//    }
//
//    @Test
//    void getUserById_ShouldReturnNull_WhenNotFound() {
//        when(userRepository.findById(any())).thenReturn(Optional.empty());
//
//        User result = userService.getUserById(new ObjectId().toHexString());
//
//        assertNull(result);
//    }
//
//    @Test
//    void isUserExists_ShouldReturnTrue_WhenExists() {
//        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);
//
//        assertTrue(userService.isUserExists("john@example.com"));
//    }
//
//    @Test
//    void isUserExists_ShouldReturnFalse_WhenNotExists() {
//        when(userRepository.existsByEmail("unknown@example.com")).thenReturn(false);
//
//        assertFalse(userService.isUserExists("unknown@example.com"));
//    }
//
//    @Test
//    void getUser_ShouldReturnUser() {
//        when(userRepository.findByEmail("john@example.com")).thenReturn(sampleUser);
//
//        User result = userService.getUser("john@example.com");
//
//        assertEquals("John", result.getFirstName());
//    }
//
//    @Test
//    void getPaginatedUsers_ShouldReturnDtoPage() {
//        Pageable pageable = PageRequest.of(0, 2, Sort.by("name").ascending());
//        Page<User> userPage = new PageImpl<>(List.of(sampleUser), pageable, 1);
//
//        when(userRepository.findAll(pageable)).thenReturn(userPage);
//
//        Page<UserProfileDto> result = userService.getPaginatedUsers(0, 2);
//
//        assertEquals(1, result.getTotalElements());
//        assertEquals("John", result.getContent().get(0).getFirstName());
//    }
//
//    @Test
//    void addUser_ShouldSaveUser_AndIncrementAnalytics() {
//        userService.addUser(sampleUser);
//
//        verify(userRepository, times(1)).save(sampleUser);
//        verify(analyticsService, times(1)).incUsers();
//    }
//
//    @Test
//    void getUserProfile_ShouldReturnFromCache_WhenCached() {
//        UserProfileDto cachedDto = new UserProfileDto();
//        cachedDto.setFirstName("CachedJohn");
//
//        when(redisService.get("user:"+sampleUser.getId().toHexString()+":profile", UserProfileDto.class))
//                .thenReturn(cachedDto);
//
//        UserProfileDto result = userService.getUserProfile(sampleUser.getId().toHexString());
//
//        assertEquals("CachedJohn", result.getFirstName());
//        verify(redisService, times(1))
//                .get("user:"+sampleUser.getId().toHexString()+":profile", UserProfileDto.class);
//        verify(userRepository, never()).findById(any());
//    }
//
//    @Test
//    void getUserProfile_ShouldFetchFromRepo_AndCache_WhenNotCached() {
//        when(redisService.get("user:"+sampleUser.getId().toHexString()+":profile", UserProfileDto.class))
//                .thenReturn(null);
//        when(userRepository.findById(sampleUser.getId())).thenReturn(Optional.of(sampleUser));
//
//        UserProfileDto result = userService.getUserProfile(sampleUser.getId().toHexString());
//
//        assertEquals("John", result.getFirstName());
//        verify(redisService, times(1)).set(eq("user:"+sampleUser.getId().toHexString()+":profile"), any(UserProfileDto.class), eq(300L));
//    }
//
//    @Test
//    void updateUserProfile_ShouldUpdateFieldsAndSave() {
//        when(userRepository.findById(sampleUser.getId())).thenReturn(Optional.of(sampleUser));
//
//        UserProfileDto dto = new UserProfileDto();
//        dto.setFirstName("UpdatedJohn");
//        dto.setPhoneNumber("9999999999");
//
//        UserProfileDto result = userService.updateUserProfile(sampleUser.getId().toHexString(), dto);
//
//        assertEquals("UpdatedJohn", result.getFirstName());
//        assertEquals("9999999999", result.getPhoneNumber());
//        verify(userRepository, times(1)).save(sampleUser);
//    }
}
