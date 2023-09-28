package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.AddNativeRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.DeleteResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UpdateUserResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.exceptions.EntityDoesNotExistException;
import com.capstoneproject.ElitesTracker.exceptions.UserExistsException;
import com.capstoneproject.ElitesTracker.models.Natives;
import com.capstoneproject.ElitesTracker.repositories.NativesRepository;
import com.capstoneproject.ElitesTracker.services.interfaces.NativesService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.NATIVE_DOES_NOT_EXIST_EXCEPTION;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.savedNativeMessage;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.userAlreadyExistsMessage;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.DELETE_USER_MESSAGE;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.PROFILE_UPDATE_SUCCESSFUL;

@Service
@AllArgsConstructor
@Slf4j
public class ElitesNativesService implements NativesService {
    private final NativesRepository nativesRepository;

    @Override
    public UserRegistrationResponse addNewNative(AddNativeRequest request) {
        checkIfNativeExists(request);

        UserRegistrationResponse response = new UserRegistrationResponse();
        Natives newNative = new Natives();
        BeanUtils.copyProperties(request, newNative);
        newNative.setSemicolonID(request.getSemicolonID().toUpperCase());
        newNative.setFirstName(request.getFirstName().toUpperCase());
        newNative.setLastName(request.getLastName().toUpperCase());
        Natives savedNative = nativesRepository.save(newNative);
        response.setMessage(savedNativeMessage(savedNative.getFirstName().toUpperCase(),savedNative.getLastName().toUpperCase(),request.getCohort()));
        return response;
    }

    @Override
    public Natives findNativeByEmail(String email) {
        Optional<Natives> semicolonNative = nativesRepository.findBySemicolonEmail(email);
        return semicolonNative.orElseThrow(()-> new EntityDoesNotExistException(NATIVE_DOES_NOT_EXIST_EXCEPTION.getMessage()));
    }

    @Override
    public UpdateUserResponse updateNativeProfile(Natives nativeToUpdate) {
        nativesRepository.save(nativeToUpdate);
        return UpdateUserResponse.builder().message(PROFILE_UPDATE_SUCCESSFUL).build();
    }

    @Override
    public Natives findNativeByCohort(String cohort) {
        Optional<Natives> semicolonNative = nativesRepository.findByCohort(cohort);
        return semicolonNative.orElseThrow(()-> new EntityDoesNotExistException(NATIVE_DOES_NOT_EXIST_EXCEPTION.getMessage()));
    }

    @Override
    public List<Natives> findAllNatives() {
        return nativesRepository.findAll();
    }

    @Override
    public Natives findNativeByEmailAndScv(String email, String scv) {
        Optional<Natives> semicolonNative = nativesRepository.findBySemicolonEmailAndSemicolonID(email, scv);
        return semicolonNative.orElseThrow(()-> new EntityDoesNotExistException(NATIVE_DOES_NOT_EXIST_EXCEPTION.getMessage()));
    }

    @Override
    public boolean isNative(String email, String scv) {
        return nativesRepository.existsBySemicolonEmailAndSemicolonID(email, scv);
    }

    @Override
    public List<Natives> findAllNativesInACohort(String cohort) {
        List<Natives> foundNatives = nativesRepository.findAll();

        List<Natives> cohortList = new ArrayList<>();
        for (int i = 0; i < foundNatives.size(); i++) {
            if(foundNatives.get(i).getCohort().equals(cohort)){
                cohortList.add(foundNatives.get(i));
            }
        }
        return cohortList;
    }

    @Override
    public DeleteResponse deleteNative(Natives nativeToBeDeleted) {
        nativesRepository.delete(nativeToBeDeleted);
        return DeleteResponse.builder()
                .message(DELETE_USER_MESSAGE)
                .build();
    }

    private void checkIfNativeExists(AddNativeRequest request) {
        List<Natives> nativesList = nativesRepository.findAll();
        for (Natives natives : nativesList) {
            if (natives.getSemicolonEmail().equalsIgnoreCase(request.getSemicolonEmail())) {
                throw new UserExistsException(userAlreadyExistsMessage(request.getSemicolonEmail()));
            }
        }
    }

}
