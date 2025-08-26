package com.example.carpooling.services;

import com.example.carpooling.dto.SosAuthorityMapper;
import com.example.carpooling.entities.SosAuthorities;
import com.example.carpooling.repositories.SosAuthoritesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SosAuthoritiesServiceTest {

//    @Mock
//    private SosAuthoritesRepository sosAuthoritesRepository;
//
//    @InjectMocks
//    private SosAuthoritiesService sosAuthoritiesService;
//
//    private SosAuthorityMapper mapper;
//    private SosAuthorities authority;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        mapper = new SosAuthorityMapper();
//        mapper.setLabel("Police");
//        mapper.setEmail("police@example.com");
//
//        authority = new SosAuthorities("Police", "police@example.com");
//        authority.setId(new org.bson.types.ObjectId());
//    }
//
//    @Test
//    void addAuthority_ShouldSaveAndReturnAuthority() {
//        when(sosAuthoritesRepository.save(any(SosAuthorities.class))).thenReturn(authority);
//
//        SosAuthorities result = sosAuthoritiesService.addAuthority(mapper);
//
//        assertEquals("Police", result.getLabel());
//        assertEquals("police@example.com", result.getEmail());
//        verify(sosAuthoritesRepository, times(1)).save(any(SosAuthorities.class));
//    }
//
//    @Test
//    void getAuthority_ShouldReturnAuthority_WhenExists() {
//        when(sosAuthoritesRepository.findByLabel("Police")).thenReturn(authority);
//
//        SosAuthorities result = sosAuthoritiesService.getAuthority("Police");
//
//        assertEquals("Police", result.getLabel());
//        assertEquals("police@example.com", result.getEmail());
//        verify(sosAuthoritesRepository, times(1)).findByLabel("Police");
//    }
//
//    @Test
//    void updateAuthority_ShouldUpdateEmail_WhenCurrentEmailExists() {
//        when(sosAuthoritesRepository.findByLabel("Police")).thenReturn(authority);
//        when(sosAuthoritesRepository.save(any(SosAuthorities.class))).thenReturn(authority);
//
//        SosAuthorityMapper updateMapper = new SosAuthorityMapper();
//        updateMapper.setEmail("newpolice@example.com");
//
//        SosAuthorities result = sosAuthoritiesService.updateAuthority("Police", updateMapper);
//
//        assertEquals("Police", result.getLabel());
//        assertEquals("newpolice@example.com", result.getEmail());
//        verify(sosAuthoritesRepository, times(1)).findByLabel("Police");
//        verify(sosAuthoritesRepository, times(1)).save(authority);
//    }
//
//    @Test
//    void updateAuthority_ShouldNotChangeEmail_WhenCurrentEmailIsNullOrEmpty() {
//        authority.setEmail(null);
//        when(sosAuthoritesRepository.findByLabel("Police")).thenReturn(authority);
//        when(sosAuthoritesRepository.save(any(SosAuthorities.class))).thenReturn(authority);
//
//        SosAuthorityMapper updateMapper = new SosAuthorityMapper();
//        updateMapper.setEmail("newpolice@example.com");
//
//        SosAuthorities result = sosAuthoritiesService.updateAuthority("Police", updateMapper);
//
//        assertNull(result.getEmail());
//        verify(sosAuthoritesRepository, times(1)).findByLabel("Police");
//        verify(sosAuthoritesRepository, times(1)).save(authority);
//    }
}
