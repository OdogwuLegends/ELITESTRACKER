package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.MockDbRequest;
import com.capstoneproject.ElitesTracker.dtos.responses.UserRegistrationResponse;
import com.capstoneproject.ElitesTracker.exceptions.NativeDoesNotExistException;
import com.capstoneproject.ElitesTracker.models.MockSemicolonDB;
import com.capstoneproject.ElitesTracker.repositories.MockSemicolonDbRepository;
import com.capstoneproject.ElitesTracker.services.interfaces.SemicolonDbService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.NATIVE_DOES_NOT_EXIST_EXCEPTION;
import static com.capstoneproject.ElitesTracker.utils.App.saveNativeMessage;

@Service
@AllArgsConstructor
public class MockSemicolonDbService implements SemicolonDbService {
    private final MockSemicolonDbRepository mockSemicolonDbRepository;

    @Override
    public UserRegistrationResponse registerNative(MockDbRequest request) {
        MockSemicolonDB newNative = new MockSemicolonDB();
        BeanUtils.copyProperties(request, newNative);
        MockSemicolonDB savedNative = mockSemicolonDbRepository.save(newNative);

        return UserRegistrationResponse.builder()
                .message(saveNativeMessage(savedNative.getFirstName().toUpperCase(),savedNative.getLastName().toUpperCase()))
                .build();
    }

    @Override
    public MockSemicolonDB findNative(String email) {
        Optional<MockSemicolonDB> semicolonNative = mockSemicolonDbRepository.findBySemicolonEmail(email);
        return semicolonNative.orElseThrow(()-> new NativeDoesNotExistException(NATIVE_DOES_NOT_EXIST_EXCEPTION.getMessage()));
    }

}
