package com.capstoneproject.ElitesTracker.services.interfaces;

import com.capstoneproject.ElitesTracker.dtos.requests.AddAdminRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.DeleteRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.DeleteResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.models.Admins;

public interface AdminsService {
    UserRegistrationResponse addNewAdmin(AddAdminRequest request);
    DeleteResponse removeAdmin(DeleteRequest request);
    Admins findAdminByEmail(String email);
    boolean isExistingAdmin(String email);

}
