package com.capstoneproject.ElitesTracker.services.implementation;

import com.capstoneproject.ElitesTracker.dtos.requests.*;
import com.capstoneproject.ElitesTracker.dtos.responses.*;
import com.capstoneproject.ElitesTracker.emailConfig.EmailService;
import com.capstoneproject.ElitesTracker.enums.AdminPrivileges;
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
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.util.*;

import static com.capstoneproject.ElitesTracker.enums.AdminPrivileges.*;
import static com.capstoneproject.ElitesTracker.enums.AttendancePermission.ENABLED;
import static com.capstoneproject.ElitesTracker.enums.ExceptionMessages.*;
import static com.capstoneproject.ElitesTracker.enums.Role.ADMIN;
import static com.capstoneproject.ElitesTracker.enums.Role.NATIVE;
import static com.capstoneproject.ElitesTracker.security.jwt.JwtUtil.*;
import static com.capstoneproject.ElitesTracker.utils.AppUtil.*;
import static com.capstoneproject.ElitesTracker.utils.HardCoded.*;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
@ToString
public class EliteUserService implements UserService {
    private final EliteUserRepository eliteUserRepository;
    private final AdminsService adminsService;
    private final NativesService nativesService;
    private final AttendanceService attendanceService;
    private final SearchService searchService;
    private final TimeEligibilityService timeEligibilityService;
    private EmailService emailService;
//    private final PasswordEncoder passwordEncoder;


    @Override
    public UserRegistrationResponse registerUser(UserRegistrationRequest request) throws EntityDoesNotExistException {

        checkIfUserExists(request);

        UserRegistrationResponse response = new UserRegistrationResponse();
        checkIfAdminOrNative(request, response);
        return response;
    }

    @Override
    public LoginResponse loginUser(LoginRequest request) {
        String email = request.getSemicolonEmail().trim();
        Optional<EliteUser> foundUser = eliteUserRepository.findBySemicolonEmail(email);

        if(foundUser.isEmpty()){
            throw new IncorrectDetailsException(USERNAME_NOT_CORRECT_EXCEPTION.getMessage());
        }
        if(!foundUser.get().getPassword().equals(request.getPassword())){
            //passwordEncoder.matches(request.getPassword(),foundUser.get().getPassword()
            throw new IncorrectDetailsException(PASSWORD_NOT_CORRECT_EXCEPTION.getMessage());
        }

        return LoginResponse.builder()
                .message(LOGIN_MESSAGE)
                .semicolonEmail(foundUser.get().getSemicolonEmail())
                .firstName(foundUser.get().getFirstName())
                .jwtToken(generateAccessTokenWithOutSecurity(foundUser.get().getSemicolonEmail()))
                .isLoggedIn(true)
                .build();
    }

    @Override
    public EliteUser findUserByEmail(String email) {
        return eliteUserRepository.findBySemicolonEmail(email).orElseThrow(
                ()-> new EntityDoesNotExistException(USER_DOES_NOT_EXIST_EXCEPTION.getMessage()));
    }

//    @Override
//    public UpdateUserResponse updateUserProfile(UpdateUserRequest request) {
//        EliteUser foundUser = findUserByEmail(request.getSemicolonEmail());
//        Natives foundNative = nativesService.findNativeByEmail(request.getSemicolonEmail());
////        UpdateUserRequest editedRequest = editFieldsToUppercase(request);
//        JsonPatch updatePatch = buildUpdatePatch(request);
//        return applyPatch(updatePatch, foundUser, foundNative);
//    }
    @Override
    public UpdateUserResponse updateUserProfile(UpdateUserRequest request) {
//        EliteUser foundAdmin = findUserByEmail(request.getAdminSemicolonEmail());
//        checkForSubAdminPrivilege(foundAdmin);

        editToUpperCase(request);
        return UpdateUserResponse.builder()
                .message(PROFILE_UPDATE_SUCCESSFUL)
                .build();
    }

    @Override
    public ResetPasswordResponse sendEmailForPasswordReset(ResetPasswordRequest request) {
        String userEmail = request.getSemicolonEmail().replaceAll("\"", "");

        EliteUser foundUser = findUserByEmail(userEmail);

        String token = generateRandomToken();

        try {
            emailService.sendResetPasswordEmail(token,foundUser);
            foundUser.setResetPasswordToken(token);
            eliteUserRepository.save(foundUser);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e.getMessage());
        }

        return ResetPasswordResponse.builder()
                .message(EMAIL_SENT_SUCCESSFULLY)
                .isValidEmail(true)
                .build();
    }

    @Override
    public ResetPasswordResponse resetPassword(ResetPasswordRequest request) {
        String token = request.getToken().replaceAll("\"", "");

        EliteUser foundUser = eliteUserRepository.findByResetPasswordToken(token).orElseThrow(
                ()-> new EntityDoesNotExistException(INVALID_TOKEN_EXCEPTION.getMessage()));

        foundUser.setPassword(request.getNewPassword()); //encrypt if security
        foundUser.setResetPasswordToken(null);
        eliteUserRepository.save(foundUser);

        return ResetPasswordResponse.builder()
                .message(PASSWORD_RESET_SUCCESSFUL)
                .build();
    }

    @Override
    public AttendanceResponse takeAttendance(AttendanceRequest request) {
//        checkForAdmin(request);
//        String verifiedToken = retrieveAndVerifyJwtToken(httpServletRequest);
//        String userEmail = extractEmailFromToken(verifiedToken);
        String deviceId = "";
        try {
            deviceId = getDeviceId(request.getFiftyOneDegrees());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


//        log.info("Device ID {}", deviceId);

        String changedFormat =  changeDateFormatFromFrontendForAttendance(request.getAttendanceDate());
        request.setAttendanceDate(changedFormat);

        String userEmail = request.getSemicolonEmail().replaceAll("\"", "");
//        String newHeight = request.getScreenHeight().replaceAll("\"", "");
//        String newWidth = request.getScreenWidth().replaceAll("\"", "");
//
//        request.setScreenHeight(newHeight);
//        request.setScreenWidth(newWidth);

        EliteUser foundUser = findUserByEmail(userEmail);

        return attendanceService.saveAttendance(request,foundUser);
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

        String startDate = changeDateFormatFromFrontendForReports(request.getStartDate());
        String endDate = changeDateFormatFromFrontendForReports(request.getEndDate());

        request.setStartDate(startDate);
        request.setEndDate(endDate);

        String userEmail = request.getNativeSemicolonEmail().replaceAll("\"", "");

        EliteUser foundUser = findUserByEmail(userEmail);
        return searchService.searchAttendanceReportForSelf(request, foundUser);
    }

    @Override
    public AttendanceResponse editAttendanceStatus(EditAttendanceRequest request) {
//        EliteUser foundAdmin = findUserByEmail(request.getAdminSemicolonEmail());
//        checkForSubAdminPrivilege(foundAdmin);

        EliteUser foundUser = findUserByEmail(request.getNativeSemicolonEmail());
        return attendanceService.editAttendanceStatus(request, foundUser);
    }

    @Override
    public TimeResponse setTimeForAttendance(SetTimeRequest request) {
//        EliteUser foundAdmin = findUserByEmail(request.getAdminSemicolonEmail());
//        checkForSubAdminPrivilege(foundAdmin);
        return timeEligibilityService.setTimeForAttendance(request);
    }

    @Override
    public List<AttendanceSheetResponse> generateAttendanceReportForNative(SearchRequest request) {
//        EliteUser foundAdmin = findUserByEmail(request.getAdminSemicolonEmail());
//        checkForSubAdminPrivilege(foundAdmin);

        EliteUser foundNative = findUserByEmail(request.getNativeSemicolonEmail());
        return searchService.searchAttendanceReportForNative(request,foundNative);
    }


    @Override
    public List<AttendanceSheetResponse> generateAttendanceReportForCohort(SearchRequest request) {
//        EliteUser foundAdmin = findUserByEmail(request.getAdminSemicolonEmail());
//        checkForSubAdminPrivilege(foundAdmin);
        return searchService.searchAttendanceReportForCohort(request);
    }

    @Override
    public PermissionForAttendanceResponse setAttendancePermissionForNative(PermissionForAttendanceRequest request) {
//        EliteUser foundAdmin = findUserByEmail(request.getAdminSemicolonEmail());
//        checkForSubAdminPrivilege(foundAdmin);

        EliteUser foundUser = findUserByEmail(request.getNativeSemicolonEmail());
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
//        EliteUser foundAdmin = findUserByEmail(request.getAdminSemicolonEmail());
//        checkForSubAdminPrivilege(foundAdmin);

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
    public List<EliteUser> findAllNatives(){
        List<EliteUser> allUsers = eliteUserRepository.findAll();
        List<EliteUser> allNatives = new ArrayList<>();

        for(EliteUser user : allUsers){
            if(user.getRole().equals(NATIVE)){
                allNatives.add(user);
            }
        }
        return allNatives;
    }
    @Override
    public DeleteResponse removeNative(DeleteRequest request) {
//        EliteUser foundAdmin = findUserByEmail(request.getAdminSemicolonEmail());
//        checkForSuperAdminPrivilege(foundAdmin);

        EliteUser foundUser = findUserByEmail(request.getNativeSemicolonEmail());
        if(!foundUser.getCohort().equals(request.getCohort())){
            throw new EntityDoesNotExistException(nativeNotFoundMessage(request.getCohort()));
        }
        Natives foundNative = nativesService.findNativeByEmail(request.getNativeSemicolonEmail());
        eliteUserRepository.delete(foundUser);
        nativesService.deleteNative(foundNative);
        return DeleteResponse.builder()
                .message(DELETE_USER_MESSAGE)
                .build();
    }

    @Override
    public DeleteResponse removeAdmin(DeleteRequest request) {
//        EliteUser foundAdmin = findUserByEmail(request.getAdminSemicolonEmail());
//        checkForSuperAdminPrivilege(foundAdmin);

        EliteUser foundUser = findUserByEmail(request.getAdminSemicolonEmail());
        Admins adminToRemove = adminsService.findAdminByEmail(request.getAdminSemicolonEmail());
        eliteUserRepository.delete(foundUser);
        adminsService.removeAdmin(adminToRemove);
        return DeleteResponse.builder()
                .message(DELETE_USER_MESSAGE)
                .build();
    }

    @Override
    public DeleteResponse removeCohort(DeleteRequest request) {
//        EliteUser foundAdmin = findUserByEmail(request.getAdminSemicolonEmail());
//        checkForSuperAdminPrivilege(foundAdmin);

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
        log.info("Native Email {} length {}", request.getNativeSemicolonEmail(), request.getNativeSemicolonEmail().length());
        EliteUser foundAdmin = findUserByEmail(request.getAdminSemicolonEmail());
//        checkForSubAdminPrivilege(foundAdmin);

        LoginRequest loginRequest = LoginRequest.builder()
                .semicolonEmail(request.getAdminSemicolonEmail())
                .password(request.getAdminPassword())
                .build();
        loginUser(loginRequest);
//        passwordEncoder.matches(foundAdmin.getPassword(),request.getAdminPassword());
        String userEmail = request.getNativeSemicolonEmail().replaceAll("\"", "");

        EliteUser foundNative = findUserByEmail(userEmail);
        foundNative.setScreenWidth(request.getScreenWidth());
        foundNative.setScreenHeight(request.getScreenHeight());
        eliteUserRepository.save(foundNative);
        return ResetDeviceResponse.builder().message(DEVICE_RESET_MESSAGE).build();
    }


    @Override
    @Scheduled(cron = "0 0 18 ? * MON-FRI")
    public void setToAbsent() {
        attendanceService.setToAbsent(findAllNatives());
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
//                .password(passwordEncoder.encode(request.getPassword()))
                .password(request.getPassword())
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
//                .password(passwordEncoder.encode(request.getPassword()))
                .password(request.getPassword())
                .role(ADMIN)
                .adminPrivilegesList(setSubAdmin())
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
    private void editToUpperCase(UpdateUserRequest request) {
        EliteUser foundUser = findUserByEmail(request.getSemicolonEmail());
        if(Objects.nonNull(request.getFirstName()) && !request.getFirstName().equals(""))foundUser.setFirstName(request.getFirstName().toUpperCase());
        if(Objects.nonNull(request.getLastName()) && !request.getLastName().equals(""))foundUser.setLastName(request.getLastName().toUpperCase());
        if(Objects.nonNull(request.getCohort()) && !request.getCohort().equals(""))foundUser.setCohort(request.getCohort().toUpperCase());
        if(Objects.nonNull(request.getSemicolonID()) && !request.getSemicolonID().equals(""))foundUser.setSemicolonID(request.getSemicolonID().toUpperCase());
        if(Objects.nonNull(request.getUpdatedSemicolonEmail()) && !request.getUpdatedSemicolonEmail().equals(""))foundUser.setSemicolonEmail(request.getUpdatedSemicolonEmail());
        eliteUserRepository.save(foundUser);
    }

    private UpdateUserResponse applyPatch(JsonPatch updatePatch, EliteUser eliteUser, Natives foundNative) {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode userNode = objectMapper.convertValue(eliteUser, JsonNode.class);
        JsonNode nativeNode = objectMapper.convertValue(foundNative, JsonNode.class);
        try {
            JsonNode userUpdatedNode = updatePatch.apply(userNode);
            JsonNode nativeUpdatedNode = updatePatch.apply(nativeNode);
            EliteUser updatedUser = objectMapper.convertValue(userUpdatedNode, EliteUser.class);
            Natives updatedNative = objectMapper.convertValue(nativeUpdatedNode,Natives.class);
            eliteUserRepository.save(updatedUser);
            nativesService.updateNativeProfile(updatedNative);
            return new UpdateUserResponse(PROFILE_UPDATE_SUCCESSFUL);
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

        field.setAccessible(true);
        try {
            return field.get(updateUserRequest) != null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static ReplaceOperation buildReplaceOperation(UpdateUserRequest updateUserRequest, Field field) {
        field.setAccessible(true);
        try {
            String path = JSON_PATCH_PATH_PREFIX + field.getName();
            JsonPointer pointer = new JsonPointer(path);
            String value = field.get(updateUserRequest).toString().toUpperCase();
            TextNode node = new TextNode(value);
            return new ReplaceOperation(pointer, node);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
    private static UpdateUserRequest editFieldsToUppercase(UpdateUserRequest request){
        return UpdateUserRequest.builder()
                .firstName(request.getFirstName().toUpperCase())
                .lastName(request.getLastName().toUpperCase())
                .semicolonID(request.getSemicolonID().toUpperCase())
                .cohort(request.getCohort())
                .semicolonEmail(request.getSemicolonEmail())
                .build();
    }
    private static void checkForAdmin(AttendanceRequest request) {
        if(!request.getSemicolonEmail().contains(NATIVE_CHECK)){
            throw new AdminsNotPermittedException(ADMIN_NOT_PERMITTED_FOR_OPERATION_EXCEPTION.getMessage());
        }
    }
    private static Set<AdminPrivileges> setSubAdmin(){
        Set<AdminPrivileges> privilege = new TreeSet<>();
        privilege.add(CLASS_PARENT);
        return privilege;
    }

    private void checkForSubAdminPrivilege(EliteUser foundAdmin) {
        if(!foundAdmin.getAdminPrivilegesList().contains(SUB_ADMIN)){
            throw new AdminsNotPermittedException(PRIVILEGE_NOT_GRANTED_EXCEPTION.getMessage());
        }
    }
    private void checkForSuperAdminPrivilege(EliteUser foundAdmin) {
        if(!foundAdmin.getAdminPrivilegesList().contains(SUPER_ADMIN)){
            throw new AdminsNotPermittedException(PRIVILEGE_NOT_GRANTED_EXCEPTION.getMessage());
        }
    }
    private static String generateRandomToken(){
        String token = "";
        Random random = new Random();

        while (token.length() < 4){
            int number = random.nextInt(9999);
            token = String.valueOf(number);
        }
        return token;
    }
}
