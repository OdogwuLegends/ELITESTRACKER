package com.capstoneproject.ElitesTracker.services.interfaces;

import com.capstoneproject.ElitesTracker.dtos.requests.MockDbRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.models.MockSemicolonDB;

public interface SemicolonDbService {
    UserRegistrationResponse registerNative(MockDbRequest request);

    MockSemicolonDB findNative(String email);
}
