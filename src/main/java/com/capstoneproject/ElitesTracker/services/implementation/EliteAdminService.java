package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.AddAdminRequest;
import com.capstoneproject.ElitesTracker.dtos.requests.DeleteRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.DeleteResponse;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.exceptions.EntityDoesNotExistException;
import com.capstoneproject.ElitesTracker.models.Admins;
import com.capstoneproject.ElitesTracker.repositories.AdminsRepository;
import com.capstoneproject.ElitesTracker.services.interfaces.AdminsService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.ADMIN_DOES_NOT_EXIST_EXCEPTION;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.savedNameMessage;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.DELETE_USER_MESSAGE;

@Service
@AllArgsConstructor
public class EliteAdminService implements AdminsService {
    private final AdminsRepository adminsRepository;

    @Override
    public UserRegistrationResponse addNewAdmin(AddAdminRequest request) {
        UserRegistrationResponse response = new UserRegistrationResponse();
        Admins newAdmin = new Admins();
        BeanUtils.copyProperties(request,newAdmin);
        newAdmin.setFirstName(request.getFirstName().toUpperCase());
        newAdmin.setLastName(request.getLastName().toUpperCase());
        Admins savedAdmin = adminsRepository.save(newAdmin);
        response.setMessage(savedNameMessage(savedAdmin.getFirstName().toUpperCase(),savedAdmin.getLastName().toUpperCase()));
        return response;
    }

    @Override
    public DeleteResponse removeAdmin(DeleteRequest request) {
        Admins foundAdmin = findAdminByEmail(request.getSemicolonEmail());
        adminsRepository.delete(foundAdmin);
        return DeleteResponse.builder()
                .message(DELETE_USER_MESSAGE)
                .build();
    }

    @Override
    public Admins findAdminByEmail(String email) {
        Optional<Admins> foundAdmin = adminsRepository.findBySemicolonEmail(email);
        return foundAdmin.orElseThrow(()-> new EntityDoesNotExistException(ADMIN_DOES_NOT_EXIST_EXCEPTION.getMessage()));
    }

    @Override
    public boolean isExistingAdmin(String email) {
        return adminsRepository.existsBySemicolonEmail(email);
    }

}
