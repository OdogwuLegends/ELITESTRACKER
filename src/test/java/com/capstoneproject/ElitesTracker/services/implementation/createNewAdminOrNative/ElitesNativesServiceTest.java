package com.capstoneproject.ElitesTracker.services.implementation.createNewAdminOrNative;

import com.capstoneproject.ElitesTracker.dtos.requests.AddNativeRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.exceptions.EntityDoesNotExistException;
import com.capstoneproject.ElitesTracker.models.Natives;
import com.capstoneproject.ElitesTracker.services.implementation.ElitesNativesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class ElitesNativesServiceTest {
    @Autowired
    private ElitesNativesService elitesNativesService;
    private UserRegistrationResponse response;


    @Test
    void addNewNative() {
        response = elitesNativesService.addNewNative(buildCephas());
        response = elitesNativesService.addNewNative(buildNed());
        assertNotNull(response);
        assertThat(response.getMessage()).isNotNull();
    }

    @Test
    void findNativeByEmail() {
        response = elitesNativesService.addNewNative(buildGoodness());
        String email = "g.obinali@native.semicolon.africa";
        Natives foundNative = elitesNativesService.findNativeByEmail(email);
        assertThat(foundNative).isNotNull();
        assertEquals("GOODNESS", foundNative.getFirstName());
    }

    @Test
    void findNativeWithWrongEmailThrowsError(){
       response = elitesNativesService.addNewNative(buildLegend());
       String email = "legend@native.semicolon.africa";
       assertThrows(EntityDoesNotExistException.class,()->elitesNativesService.findNativeByEmail(email));
    }

    @Test
    void findNativeByEmailAndScv() {
        String email = "h.cephas@native.semicolon.africa";
        String scv = "scv15006";
        Natives foundNative = elitesNativesService.findNativeByEmailAndScv(email,scv);
        assertThat(foundNative).isNotNull();
        assertEquals("HEMBA", foundNative.getLastName());
        assertEquals("15", foundNative.getCohort());
    }
    @Test
    void findNativeWithWrongSvcThrowsError(){
        response = elitesNativesService.addNewNative(buildInem());
        String email = "i.udousoro@native.semicolon.africa";
        String scv = "scv15003";
        assertThrows(EntityDoesNotExistException.class,()-> elitesNativesService.findNativeByEmailAndScv(email,scv));
    }

    @Test
    void isNative() {
        String email = "b.osisiogu@native.semicolon.africa";
        String scv = "scv15007";
        boolean isExistingNative = elitesNativesService.isNative(email,scv);
        assertTrue(isExistingNative);
    }

    private AddNativeRequest buildGoodness(){
        return AddNativeRequest.builder()
                .firstName("Goodness")
                .lastName("Obinali")
                .cohort("15")
                .semicolonEmail("g.obinali@native.semicolon.africa")
                .semicolonID("SCV15005")
                .build();
    }
    private AddNativeRequest buildCephas(){
        return AddNativeRequest.builder()
                .firstName("Cephas")
                .lastName("Hemba")
                .cohort("15")
                .semicolonEmail("h.cephas@native.semicolon.africa")
                .semicolonID("SCV15006")
                .build();
    }
    private AddNativeRequest buildNed(){
        return AddNativeRequest.builder()
                .firstName("Benjamin")
                .lastName("Osisiogu")
                .cohort("15")
                .semicolonEmail("b.osisiogu@native.semicolon.africa")
                .semicolonID("SCV15007")
                .build();
    }
    private AddNativeRequest buildLegend(){
        return AddNativeRequest.builder()
                .firstName("Odogwu")
                .lastName("Legend")
                .cohort("15")
                .semicolonEmail("l.odogwu@native.semicolon.africa")
                .semicolonID("SCV15008")
                .build();
    }
    private AddNativeRequest buildInem(){
        return AddNativeRequest.builder()
                .firstName("Inemesit")
                .lastName("Udousoro")
                .cohort("15")
                .semicolonEmail("i.udousoro@native.semicolon.africa")
                .semicolonID("SCV15009")
                .build();
    }
}