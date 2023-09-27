package com.capstoneproject.ElitesTracker.services.implementation.mockito;

import com.capstoneproject.ElitesTracker.dtos.requests.AddAdminRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.exceptions.EntityDoesNotExistException;
import com.capstoneproject.ElitesTracker.models.Admins;
import com.capstoneproject.ElitesTracker.repositories.AdminsRepository;
import com.capstoneproject.ElitesTracker.services.implementation.EliteAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static com.capstoneproject.ElitesTracker.utils.AppUtil.savedAdminMessage;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class MockEliteAdminServiceTest {
    @Mock
    private AdminsRepository adminsRepository;
    @InjectMocks
    private EliteAdminService eliteAdminService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addANewAdmin(){
        when(adminsRepository.save(any())).thenReturn(buildMockAdmin());

        UserRegistrationResponse response = eliteAdminService.addNewAdmin(buildAdminRequest());
        assertNotNull(response);
        assertEquals(savedAdminMessage("PATIENCE","PAT"),response.getMessage());
    }

    @Test
    void findAdminByEmail(){
        when(adminsRepository.findBySemicolonEmail(any())).thenReturn(Optional.ofNullable(buildMockAdmin()));

        Admins foundAdmin = eliteAdminService.findAdminByEmail("patience@semicolon.africa");
        assertNotNull(foundAdmin);
        assertEquals("Pat",foundAdmin.getLastName());
    }
    @Test
    void findAdminWithWrongEmailThrowsException(){
        when(adminsRepository.findBySemicolonEmail("patience@semicolon.africa")).thenReturn(Optional.ofNullable(buildMockAdmin()));
        assertThrows(EntityDoesNotExistException.class,()->eliteAdminService.findAdminByEmail("theguy@semicolon.africa"));
    }
    @Test
    void testForExistingAdmin(){
        when(adminsRepository.existsBySemicolonEmail("patience@semicolon.africa")).thenReturn(true);

        boolean isExistingAdmin = eliteAdminService.isExistingAdmin("patience@semicolon.africa");
        assertTrue(isExistingAdmin);
    }

    private Admins buildMockAdmin(){
        return Admins.builder()
                .firstName("Patience")
                .lastName("Pat")
                .semicolonEmail("patience@semicolon.africa")
                .build();
    }
    private AddAdminRequest buildAdminRequest(){
        return AddAdminRequest.builder()
                .firstName("Patience")
                .lastName("Pat")
                .adminSemicolonEmail("patience@semicolon.africa")
                .build();
    }
}
