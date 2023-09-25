package com.capstoneproject.ElitesTracker.services.interfaces;

import com.capstoneproject.ElitesTracker.dtos.requests.AddNativeRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.DeleteResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UpdateUserResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.models.Natives;

import java.util.List;

public interface NativesService {
    UserRegistrationResponse addNewNative(AddNativeRequest request);
    Natives findNativeByEmail(String email);
    UpdateUserResponse updateNativeProfile(Natives nativeToUpdate);
    Natives findNativeByCohort(String cohort);
    List<Natives> findAllNatives();
    Natives findNativeByEmailAndScv(String email, String scv);
    List<Natives> findAllNativesInACohort(String cohort);
    boolean isNative(String email, String scv);
    DeleteResponse deleteNative(Natives nativeToBeDeleted);

}
