package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.*;
import com.capstoneproject.ElitesTracker.dtos.responses.*;
import com.capstoneproject.ElitesTracker.exceptions.AdminsNotPermittedException;
import com.capstoneproject.ElitesTracker.exceptions.EntityDoesNotExistException;
import com.capstoneproject.ElitesTracker.exceptions.IncorrectDetailsException;
import com.capstoneproject.ElitesTracker.exceptions.UserExistsException;
import com.capstoneproject.ElitesTracker.models.Admins;
import com.capstoneproject.ElitesTracker.models.EliteUser;
import com.capstoneproject.ElitesTracker.models.Natives;
import com.capstoneproject.ElitesTracker.repositories.EliteUserRepository;
import com.capstoneproject.ElitesTracker.services.interfaces.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import com.github.fge.jsonpatch.JsonPatchOperation;
import com.github.fge.jsonpatch.ReplaceOperation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.capstoneproject.ElitesTracker.enums.AttendancePermission.ENABLED;
import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.*;
import static com.capstoneproject.ElitesTracker.enums.Role.ADMIN;
import static com.capstoneproject.ElitesTracker.enums.Role.NATIVE;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.*;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.*;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class EliteUserService implements UserService {
    private final EliteUserRepository eliteUserRepository;
    private final AdminsService adminsService;
    private final NativesService nativesService;
    private final AttendanceService attendanceService;
    private final SearchService searchService;
    private final TimeEligibilityService timeEligibilityService;


    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) throws EntityDoesNotExistException {

        checkIfUserExists(request);

        UserRegistrationResponse response = new UserRegistrationResponse();
        checkIfAdminOrNative(request, response);
        return response;
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        Optional<EliteUser> foundUser = eliteUserRepository.findBySemicolonEmail(request.getSemicolonEmail());

        if(foundUser.isEmpty()){
            throw new IncorrectDetailsException(USERNAME_NOT_CORRECT_EXCEPTION.getMessage());
        }
        if(!foundUser.get().getPassword().equals(request.getPassword())){
            throw new IncorrectDetailsException(PASSWORD_NOT_CORRECT_EXCEPTION.getMessage());
        }

        return LoginResponse.builder()
                .message(LOGIN_MESSAGE)
                .semicolonEmail(foundUser.get().getSemicolonEmail())
                .isLoggedIn(true)
                .build();
    }

    @Override
    public EliteUser findUserByEmail(String email) {
        return eliteUserRepository.findBySemicolonEmail(email).orElseThrow(
                ()-> new EntityDoesNotExistException(USER_DOES_NOT_EXIST_EXCEPTION.getMessage()));
    }

    @Override
    public UpdateUserResponse updateUserProfile(UpdateUserRequest request) {
        EliteUser foundUser = findUserByEmail(request.getSemicolonEmail());
        JsonPatch updatePatch = buildUpdatePatch(request);
        return applyPatch(updatePatch, foundUser);
    }

    @Override
    public AttendanceResponse takeAttendance(AttendanceRequest request, HttpServletRequest httpServletRequest) {
        if(!request.getSemicolonEmail().contains(NATIVE_CHECK)){
            throw new AdminsNotPermittedException(ADMIN_NOT_PERMITTED_FOR_OPERATION_EXCEPTION.getMessage());
        }
        EliteUser foundUser = findUserByEmail(request.getSemicolonEmail());

        return attendanceService.saveAttendance(request,httpServletRequest,foundUser);
    }
    @Override
    public AttendanceResponse takeAttendanceTest(AttendanceRequest request, String IpAddress) {
        if(!request.getSemicolonEmail().contains(NATIVE_CHECK)){
            throw new AdminsNotPermittedException(ADMIN_NOT_PERMITTED_FOR_OPERATION_EXCEPTION.getMessage());
        }
        EliteUser foundUser = findUserByEmail(request.getSemicolonEmail());
        return attendanceService.saveAttendanceTest(request,IpAddress,foundUser);
    }

    @Override
    public List<AttendanceSheetResponse> generateAttendanceReportForSelf(SearchRequest request) {
        EliteUser foundUser = findUserByEmail(request.getSemicolonEmail());
        return searchService.searchAttendanceReportForSelf(request, foundUser);
    }

    @Override
    public AttendanceResponse editAttendanceStatus(EditAttendanceRequest request) {
        EliteUser foundUser = findUserByEmail(request.getSemicolonEmail());
        return attendanceService.editAttendanceStatus(request, foundUser);
    }

    @Override
    public TimeResponse setTimeForAttendance(SetTimeRequest request) {
        return timeEligibilityService.setTimeForAttendance(request);
    }

    @Override
    public List<AttendanceSheetResponse> generateAttendanceReportForNative(SearchRequest request) {
        EliteUser foundUser = findUserByEmail(request.getSemicolonEmail());
        return searchService.searchAttendanceReportForNative(request,foundUser);
    }


    @Override
    public List<AttendanceSheetResponse> generateAttendanceReportForCohort(SearchRequest request) {
        return searchService.searchAttendanceReportForCohort(request);
    }

    @Override
    public PermissionForAttendanceResponse setAttendancePermissionForNative(PermissionForAttendanceRequest request) {
        EliteUser foundUser = findUserByEmail(request.getSemicolonEmail());
        if(!foundUser.getCohort().equals(request.getCohort())){
            throw new EntityDoesNotExistException(nativeNotFoundMessage(request.getCohort()));
        }
        foundUser.setPermission(request.getPermission());
        eliteUserRepository.save(foundUser);

        return PermissionForAttendanceResponse.builder()
                .message(PERMISSION_MODIFIED_MESSAGE)
                .build();
    }

    @Override
    public PermissionForAttendanceResponse setAttendancePermitForCohort(PermissionForAttendanceRequest request) {
        List<EliteUser> foundNatives = findAllNativesInACohort(request.getCohort());

        if(foundNatives.isEmpty()){
            throw new EntityDoesNotExistException(cohortNotFoundMessage(request.getCohort()));
        }

        for (int i = 0; i < foundNatives.size(); i++) {
            EliteUser nativeToEdit = foundNatives.get(i);
            if(nativeToEdit.getCohort().equals(request.getCohort())){
                foundNatives.get(i).setPermission(request.getPermission());
                eliteUserRepository.save(foundNatives.get(i));
            }
        }

        return PermissionForAttendanceResponse.builder()
                .message(PERMISSION_MODIFIED_MESSAGE)
                .build();
    }

    @Override
    public List<EliteUser> findAllNativesInACohort(String cohort) {
        List<EliteUser> foundNatives = eliteUserRepository.findAll();

        List<EliteUser> cohortList = new ArrayList<>();
        for (EliteUser foundNative : foundNatives) {
            if (foundNative.getCohort() != null && foundNative.getCohort().equals(cohort)) {
                cohortList.add(foundNative);
            }
        }
        return cohortList;
    }
    @Override
    public DeleteResponse removeNative(DeleteRequest request) {
        EliteUser foundUser = findUserByEmail(request.getSemicolonEmail());
        if(!foundUser.getCohort().equals(request.getCohort())){
            throw new EntityDoesNotExistException(nativeNotFoundMessage(request.getCohort()));
        }
        Natives foundNative = nativesService.findNativeByEmail(request.getSemicolonEmail());
        eliteUserRepository.delete(foundUser);
        nativesService.deleteNative(foundNative);
        return DeleteResponse.builder()
                .message(DELETE_USER_MESSAGE)
                .build();
    }

    @Override
    public DeleteResponse removeAdmin(DeleteRequest request) {
        EliteUser foundUser = findUserByEmail(request.getSemicolonEmail());
        Admins foundAdmin = adminsService.findAdminByEmail(request.getSemicolonEmail());
        eliteUserRepository.delete(foundUser);
        adminsService.removeAdmin(foundAdmin);
        return DeleteResponse.builder()
                .message(DELETE_USER_MESSAGE)
                .build();
    }

    @Override
    public DeleteResponse removeCohort(DeleteRequest request) {
        List<EliteUser> foundUserList = findAllNativesInACohort(request.getCohort());
        List<Natives> foundNativesList = nativesService.findAllNativesInACohort(request.getCohort());

        if(foundUserList.isEmpty()){
            throw new EntityDoesNotExistException(cohortNotFoundMessage(request.getCohort()));
        }

        for (int i = 0; i < foundUserList.size(); i++) {
            if(foundUserList.get(i).getCohort().equals(request.getCohort())){
                eliteUserRepository.delete(foundUserList.get(i));
                nativesService.deleteNative(foundNativesList.get(i));
            }
        }
        return DeleteResponse.builder()
                .message(DELETE_USER_MESSAGE)
                .build();
    }

    @Override
    public ResetDeviceResponse resetNativeDevice(ResetDeviceRequest request) {
        LoginRequest loginRequest = LoginRequest.builder()
                .semicolonEmail(request.getAdminSemicolonEmail())
                .password(request.getAdminPassword())
                .build();
        loginUser(loginRequest);
        EliteUser foundNative = findUserByEmail(request.getNativeSemicolonEmail());
        foundNative.setScreenWidth(request.getScreenWidth());
        foundNative.setScreenHeight(request.getScreenHeight());
        eliteUserRepository.save(foundNative);
        return ResetDeviceResponse.builder().message(DEVICE_RESET_MESSAGE).build();
    }

    private void checkIfAdminOrNative(UserRegistrationRequest request, UserRegistrationResponse response) throws EntityDoesNotExistException {
        if(request.getSemicolonEmail().contains(NATIVE_CHECK) && isNative(request)){
            Natives existingNative = getExistingNativeByEmail(request.getSemicolonEmail());
            EliteUser eliteUser = buildNative(request, existingNative);
            EliteUser savedUser = eliteUserRepository.save(eliteUser);
            response.setMessage(welcomeMessage(savedUser.getFirstName().toUpperCase()));
        } else if (!request.getSemicolonEmail().contains(NATIVES) && isAdmin(request.getSemicolonEmail())) {
            Admins existingAdmin = getExistingAdmin(request.getSemicolonEmail());
            EliteUser eliteUser = buildAdmin(request,existingAdmin);
            EliteUser savedUser = eliteUserRepository.save(eliteUser);
            response.setMessage(welcomeMessage(savedUser.getFirstName().toUpperCase()));
        } else {
            throw new EntityDoesNotExistException(USER_DOES_NOT_EXIST_EXCEPTION.getMessage());
        }
    }

    private EliteUser buildNative(UserRegistrationRequest request, Natives existingNative) {
        return EliteUser.builder()
                .firstName(existingNative.getFirstName())
                .lastName(existingNative.getLastName())
                .cohort(existingNative.getCohort())
                .semicolonEmail(existingNative.getSemicolonEmail())
                .password(request.getPassword()) //Encode password after security added
                .semicolonID(request.getScv().toUpperCase())
                .role(NATIVE)
                .permission(ENABLED)
                .screenWidth(request.getScreenWidth())
                .screenHeight(request.getScreenHeight())
                .build();

    }
    private EliteUser buildAdmin(UserRegistrationRequest request, Admins existingAdmin){
        return EliteUser.builder()
                .firstName(existingAdmin.getFirstName())
                .lastName(existingAdmin.getLastName())
                .semicolonEmail(existingAdmin.getSemicolonEmail())
                .password(request.getPassword()) //Encode password after security added
                .role(ADMIN)
                .screenWidth(request.getScreenWidth())
                .screenHeight(request.getScreenHeight())
                .build();
    }

    private void checkIfUserExists(UserRegistrationRequest request){
        List<EliteUser> eliteUserList = eliteUserRepository.findAll();
        for (EliteUser eliteUser : eliteUserList){
            if(eliteUser.getSemicolonEmail().equalsIgnoreCase(request.getSemicolonEmail())){
                throw new UserExistsException(userAlreadyExistsMessage(request.getSemicolonEmail()));
            }
        }
    }

    private Admins getExistingAdmin(String email) throws EntityDoesNotExistException {
        return adminsService.findAdminByEmail(email);
    }
    private boolean isAdmin(String email){
        return adminsService.isExistingAdmin(email);
    }

    private Natives getExistingNativeByEmail(String email) throws EntityDoesNotExistException {
        return nativesService.findNativeByEmail(email);
    }
    private boolean isNative(UserRegistrationRequest request) {
        return nativesService.isNative(request.getSemicolonEmail(),request.getScv().toUpperCase());
    }

    private UpdateUserResponse applyPatch(JsonPatch updatePatch, EliteUser eliteUser) {
        ObjectMapper objectMapper = new ObjectMapper();
        //1. Convert user to JsonNode
        JsonNode userNode = objectMapper.convertValue(eliteUser, JsonNode.class);
        try {
            //2. Apply patch to JsonNode from step 1
            JsonNode updatedNode = updatePatch.apply(userNode);
            //3. Convert updatedNode back to user
            EliteUser updatedUser = objectMapper.convertValue(updatedNode, EliteUser.class);
            //4. Save updated User
            eliteUserRepository.save(updatedUser);
            return  new UpdateUserResponse(PROFILE_UPDATE_SUCCESSFUL);
        }catch (JsonPatchException exception){
            throw new IncorrectDetailsException(exception.getMessage());
        }
    }

    private JsonPatch buildUpdatePatch(UpdateUserRequest updateUserRequest) {
        Field[] fields = updateUserRequest.getClass().getDeclaredFields();

        List<ReplaceOperation> operations = Arrays.stream(fields)
                .filter(field -> validateFields(updateUserRequest, field))
                .map(field->buildReplaceOperation(updateUserRequest, field))
                .toList();

        List<JsonPatchOperation> patchOperations = new ArrayList<>(operations);
        return new JsonPatch(patchOperations);
    }

    private static boolean validateFields(UpdateUserRequest updateUserRequest, Field field) {
        List<String> list = List.of("interests","street","houseNumber","country","state", "gender","profileImage");
        field.setAccessible(true);
        try {
            return field.get(updateUserRequest) != null && !list.contains(field.getName());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static ReplaceOperation buildReplaceOperation(UpdateUserRequest updateUserRequest, Field field) {
        field.setAccessible(true);
        try {
            String path = JSON_PATCH_PATH_PREFIX + field.getName();
            JsonPointer pointer = new JsonPointer(path);
            String value = field.get(updateUserRequest).toString();
            TextNode node = new TextNode(value);
            return new ReplaceOperation(pointer, node);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
