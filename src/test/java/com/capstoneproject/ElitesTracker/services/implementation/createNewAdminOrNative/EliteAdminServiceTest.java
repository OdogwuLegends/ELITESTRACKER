package com.capstoneproject.ElitesTracker.services.implementation.createNewAdminOrNative;

import com.capstoneproject.ElitesTracker.dtos.requests.AddAdminRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.exceptions.EntityDoesNotExistException;
import com.capstoneproject.ElitesTracker.models.Admins;
import com.capstoneproject.ElitesTracker.services.implementation.EliteAdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class EliteAdminServiceTest {
    @Autowired
    private EliteAdminService eliteAdminService;
    private UserRegistrationResponse response;

    @Test
    void addNewAdmin() {
        response = eliteAdminService.addNewAdmin(buildGabriel());
        assertNotNull(response);
        assertThat(response.getMessage()).isNotNull();
    }

    @Test
    void findAdminByEmail() {
        response = eliteAdminService.addNewAdmin(buildPatience());
        String email = "patience@semicolon.africa";
        Admins foundAdmin = eliteAdminService.findAdminByEmail(email);
        assertThat(foundAdmin).isNotNull();
        assertEquals("Pat",foundAdmin.getLastName());
    }
    @Test
    void findAdminWithWrongEmailThrowsException(){
        response = eliteAdminService.addNewAdmin(buildAnotherGuy());
        String email = "theguy@semicolon.africa";
        assertThrows(EntityDoesNotExistException.class,()-> eliteAdminService.findAdminByEmail(email));
    }

    @Test
    void isExistingAdmin() {
        response = eliteAdminService.addNewAdmin(buildNewGuy());
        String email = "newguy@semicolon.africa";
        boolean isFoundAdmin = eliteAdminService.isExistingAdmin(email);
        assertTrue(isFoundAdmin);
    }

    private AddAdminRequest buildPatience(){
        return AddAdminRequest.builder()
                .firstName("Patience")
                .lastName("Pat")
                .semicolonEmail("patience@semicolon.africa")
                .build();
    }

    private AddAdminRequest buildGabriel(){
        return AddAdminRequest.builder()
                .firstName("Gabriel")
                .lastName("Gab")
                .semicolonEmail("gabriel@semicolon.africa")
                .build();
    }

    private AddAdminRequest buildNewGuy(){
        return AddAdminRequest.builder()
                .firstName("NewGuy")
                .lastName("Guy")
                .semicolonEmail("newguy@semicolon.africa")
                .build();
    }
    private AddAdminRequest buildAnotherGuy(){
        return AddAdminRequest.builder()
                .firstName("AnotherGuy")
                .lastName("Another")
                .semicolonEmail("anotherguy@semicolon.africa")
                .build();
    }
}