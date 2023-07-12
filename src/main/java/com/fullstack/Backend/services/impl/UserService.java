package com.fullstack.Backend.services.impl;

import java.io.UnsupportedEncodingException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fullstack.Backend.dto.users.*;
import com.fullstack.Backend.entities.*;
import com.fullstack.Backend.enums.Role;
import com.fullstack.Backend.repositories.interfaces.IPasswordResetTokenRepository;
import com.fullstack.Backend.repositories.interfaces.ISystemRoleRepository;
import com.fullstack.Backend.repositories.interfaces.IVerificationTokenRepository;
import com.fullstack.Backend.responses.device.KeywordSuggestionResponse;
import com.fullstack.Backend.responses.users.JwtResponse;
import com.fullstack.Backend.responses.users.MessageResponse;
import com.fullstack.Backend.responses.users.UsersResponse;
import com.fullstack.Backend.security.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.fullstack.Backend.repositories.interfaces.IUserRepository;
import com.fullstack.Backend.services.IUserService;

import static com.fullstack.Backend.constant.constant.*;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;


@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private IUserRepository _userRepository;

    @Autowired
    private ISystemRoleRepository _systemRoleRepository;

    @Autowired
    private IVerificationTokenRepository _tokenRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private IPasswordResetTokenRepository _passwordResetTokenRepository;

    @Async
    @Override
    public CompletableFuture<User> findById(int id) {
        return CompletableFuture.completedFuture(_userRepository.findById(id).orElseThrow(null));
    }

    @Override
    public CompletableFuture<Boolean> doesUserExist(int id) {
        return CompletableFuture.completedFuture(_userRepository.existsById((long) id));
    }

    @Async
    @Override
    public CompletableFuture<User> findByUsername(String username) {
        return CompletableFuture.completedFuture(_userRepository.findByUserName(username).orElse(null));
    }

    @Async
    @Override
    public CompletableFuture<List<UserDTO>> getUserList(FilterUserDTO dto) throws ExecutionException, InterruptedException {
        formatFilter(dto);
        List<User> users = _userRepository.findAll();
        users = fetchFilteredUsers(dto, users).get();
        List<UserDTO> usersList = users.stream().map(UserDTO::new).collect(Collectors.toList());
        return CompletableFuture.completedFuture(usersList);
    }

    @Async()
    public CompletableFuture<List<User>> fetchFilteredUsers(FilterUserDTO dto, List<User> users) {
        if (dto.getBadgeId() != null)
            users = users.stream().filter(user -> user.getBadgeId().equalsIgnoreCase(dto.getBadgeId())).collect(Collectors.toList());
        if (dto.getUserName() != null)
            users = users.stream().filter(user -> user.getUserName().equalsIgnoreCase(dto.getUserName())).collect(Collectors.toList());
        if (dto.getFirstName() != null)
            users = users.stream().filter(user -> user.getFirstName().equalsIgnoreCase(dto.getFirstName())).collect(Collectors.toList());
        if (dto.getLastName() != null)
            users = users.stream().filter(user -> user.getLastName().equalsIgnoreCase(dto.getLastName())).collect(Collectors.toList());
        if (dto.getEmail() != null)
            users = users.stream().filter(user -> user.getEmail().equalsIgnoreCase(dto.getEmail())).collect(Collectors.toList());
        if (dto.getPhoneNumber() != null)
            users = users.stream().filter(user -> user.getPhoneNumber().equalsIgnoreCase(dto.getPhoneNumber())).collect(Collectors.toList());
        if (dto.getProject() != null)
            users = users.stream().filter(user -> user.getProject().equalsIgnoreCase(dto.getProject())).collect(Collectors.toList());
        return CompletableFuture.completedFuture(users);
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> authenticateUser(LoginDTO loginRequest, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return CompletableFuture.completedFuture(ResponseEntity.ok().header(
                HttpHeaders.SET_COOKIE, jwtCookie.toString()).body(new JwtResponse(
                userDetails.getUser().getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles,
                userDetails.getUser().getBadgeId(),
                userDetails.getUser().getFirstName(),
                userDetails.getUser().getLastName(),
                userDetails.getUser().getPhoneNumber(),
                userDetails.getUser().getProject())));
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> registerUser(RegisterDTO registerRequest, String siteURL) throws MessagingException, UnsupportedEncodingException {
        if (nameExists(registerRequest.getUserName())) {
            return CompletableFuture.completedFuture(ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: " + registerRequest.getUserName() + " is already taken!")));
        }

        if (emailExists(registerRequest.getEmail())) {
            return CompletableFuture.completedFuture(ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: " + registerRequest.getEmail() + " is already in use!")));
        }

        // Create new user's account
        User user = new User();
        user.setUserName(registerRequest.getUserName());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        String token = RandomString.make(64);
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        user.setProject(registerRequest.getProject());
        user.setBadgeId(registerRequest.getBadgeId());
        user.setCreatedDate(new Date());
        user.setEnabled(false);
        Set<SystemRole> roles = new HashSet<>();
        SystemRole userRole = _systemRoleRepository.findByName(Role.ROLE_USER.name())
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);
        user.setSystemRoles(roles);
        _userRepository.save(user);
        createVerificationToken(user, token);
        String verifyURL = siteURL + "/api/users/verify?code=" + token;
        sendVerificationEmail(user, verifyURL);
        return CompletableFuture.completedFuture(ResponseEntity.ok(new
                MessageResponse("User registered successfully!")));
    }

    @Override
    public void sendVerificationEmail(User user, String verifyURL) throws MessagingException {
        String toAddress = user.getEmail();
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you!<br>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(FROM_ADDRESS);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getFirstName().concat(" " + user.getLastName()));
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> verify(String verificationCode) throws ExecutionException, InterruptedException {
        VerificationToken verificationToken = getVerificationToken(verificationCode).get();
        if (verificationToken == null || verificationToken.getUser().isEnabled())
            return CompletableFuture.completedFuture(ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Sorry, we could not verify account. It maybe already verified," +
                            "or verification code is incorrect.")));

        User userByToken = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();

        if ((verificationToken.getExpiryDate().getTime() - cal.getTime().getTime()) <= 0)
            return CompletableFuture.completedFuture(ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Verification code was expired!")));

        userByToken.setEnabled(true);
        _userRepository.save(userByToken);
        return CompletableFuture.completedFuture(ResponseEntity
                .ok()
                .body(new MessageResponse("Verify successfully")));

    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> showUsersWithPaging(int pageIndex, int pageSize, String sortBy, String sortDir, FilterUserDTO dto) throws ExecutionException, InterruptedException {
        List<UserDTO> usersList = getUserList(dto).get();
        List<String> projectList = usersList.stream().map(UserDTO::getProject).distinct().toList();
        int totalElements = usersList.size();
        usersList = getPage(usersList, pageIndex, pageSize).get();
        UsersResponse response = new UsersResponse();
        response.setUsersList(usersList);
        response.setPageNo(pageIndex);
        response.setPageSize(pageSize);
        response.setTotalElements(totalElements);
        response.setTotalPages(getTotalPages(pageSize, totalElements));
        response.setProjectList(projectList);
        return CompletableFuture.completedFuture(new ResponseEntity<Object>(response, OK));
    }

    @Override
    public void createVerificationToken(User user, String token) {
        VerificationToken myToken = new VerificationToken(token, user);
        _tokenRepository.save(myToken);
    }

    @Async
    @Override
    public CompletableFuture<VerificationToken> getVerificationToken(String VerificationToken) {
        return CompletableFuture.completedFuture(_tokenRepository.findByToken(VerificationToken));
    }

    @Async
    @Override
    public CompletableFuture<PasswordResetToken> getResetPasswordToken(String token) {
        return CompletableFuture.completedFuture(_passwordResetTokenRepository.findByToken(token).orElseThrow(null));
    }

    @Async
    @Override
    public CompletableFuture<User> findByEmail(String email) {
        return CompletableFuture.completedFuture(_userRepository.findByEmail(email));
    }

    @Async
    @Override
    public CompletableFuture<User> findByToken(String token) {
        if (_passwordResetTokenRepository.findByToken(token).isEmpty()) {
            return null;
        }
        return CompletableFuture.completedFuture(_passwordResetTokenRepository.findByToken(token).get().getUser());
    }

    @Async
    @Override
    public CompletableFuture<PasswordResetToken> findUserFromResetPasswordToken(User user) {
        return CompletableFuture.completedFuture(_passwordResetTokenRepository.findByUser(user));
    }

    @Async
    @Override
    public CompletableFuture<VerificationToken> generateNewVerificationToken(String existingVerificationToken) throws ExecutionException, InterruptedException {
        VerificationToken vToken = getVerificationToken(existingVerificationToken).get();
        vToken.updateToken(RandomString.make(64));
        _tokenRepository.save(vToken);
        return CompletableFuture.completedFuture(vToken);
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> resendRegistrationToken(String siteURL, String existingToken) throws ExecutionException, InterruptedException, MessagingException {
        VerificationToken newToken = generateNewVerificationToken(existingToken).get();
        User user = newToken.getUser();
        String verifyURL = siteURL + "/api/users/verify?code=" + newToken;
        resendVerificationEmail(user, verifyURL);
        return CompletableFuture.completedFuture(ResponseEntity.ok(new MessageResponse("Resent successfully!")));
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> sendResetPasswordEmail(String siteURL, String userEmail) throws ExecutionException, InterruptedException, MessagingException {
        String token = RandomString.make(64);
        CompletableFuture<User> user = findByEmail(userEmail);
        if (user.get() == null)
            return CompletableFuture.completedFuture(ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Email is not valid!")));

        PasswordResetToken existingToken = findUserFromResetPasswordToken(user.get()).get();
        if (existingToken != null) {
            PasswordResetToken newToken = generateResetPasswordToken(existingToken.getToken()).get(); /* Change old token to new token and return it */
            User updateDuser = newToken.getUser();
            String verifyURL = siteURL + "/api/users/reset_password?token=" + newToken;
            sendResetPasswordEmail(updateDuser, verifyURL);
            return CompletableFuture.completedFuture(ResponseEntity.ok(new MessageResponse("Sent successfully!")));
        }
        createPasswordResetTokenForUser(user.get(), token);
        String verifyURL = siteURL + "/api/users/reset_password?token=" + token;
        sendResetPasswordEmail(user.get(), verifyURL);
        return CompletableFuture.completedFuture(ResponseEntity.ok(new MessageResponse("Sent successfully!")));
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> saveResetPassword(ResetPasswordDTO dto) throws ExecutionException, InterruptedException, MessagingException {
        CompletableFuture<User> user = findByToken(dto.getToken());
        if (user == null)
            return CompletableFuture.completedFuture(ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User is not existent")));

        boolean isOldPasswordSimilarToNewOne = BCrypt.checkpw(dto.getOldPassword(), user.get().getPassword());
        if (!isOldPasswordSimilarToNewOne) /* Compare old password and new password */
            return CompletableFuture.completedFuture(ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Old password is incorrect!")));

        changeUserPassword(user.get(), dto.getNewPassword());
        return CompletableFuture.completedFuture(ResponseEntity.ok(new MessageResponse("Changed successfully!")));
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> saveForgotPassword(ForgotPasswordDTO dto) throws ExecutionException, InterruptedException, MessagingException {
        CompletableFuture<User> user = findByToken(dto.getToken());
        if (user == null)
            return CompletableFuture.completedFuture(ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("User is not existent")));

        Optional<PasswordResetToken> token = _passwordResetTokenRepository.findByToken(dto.getToken());
        if(token.isEmpty()){
            return CompletableFuture.completedFuture(ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Token is not existent")));
        }

        if(!Objects.equals(dto.getNewPassword(), dto.getConfirmPassword())){
            return CompletableFuture.completedFuture(ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Password must be identical to confirm password")));
        }
        changeUserPassword(user.get(), dto.getNewPassword());
        _passwordResetTokenRepository.delete(token.get());
        return CompletableFuture.completedFuture(ResponseEntity
                .ok(new MessageResponse("Changed successfully!")));
    }

    @Async
    @Override
    public CompletableFuture<String> validatePasswordResetToken(String token) {
        final Calendar cal = Calendar.getInstance();
        final Optional<PasswordResetToken> passToken = _passwordResetTokenRepository.findByToken(token);
        if (passToken.isEmpty()) return CompletableFuture.completedFuture("not existent");
        boolean isTokenExpired = passToken.get().getExpiryDate().before(cal.getTime());
        boolean isTokenFound = passToken != null;
        return !isTokenFound ? CompletableFuture.completedFuture("invalid")
                : isTokenExpired ? CompletableFuture.completedFuture("expired")
                : null;
    }

    @Async()
    @Override
    public CompletableFuture<ResponseEntity<Object>> getSuggestKeywordUsers(int fieldColumn, String keyword, FilterUserDTO filter) throws InterruptedException, ExecutionException {
        if (keyword.trim().isBlank())
            return CompletableFuture.completedFuture(ResponseEntity.status(NOT_FOUND).body("Keyword must be non-null"));
        List<User> users = _userRepository.findAll();
        List<UserDTO> deviceList = getAllUser(users, filter).get();
        Set<String> keywordList = selectColumnForKeywordSuggestion(deviceList, keyword, fieldColumn).get();
        KeywordSuggestionResponse response = new KeywordSuggestionResponse();
        response.setKeywordList(keywordList);
        return CompletableFuture.completedFuture(new ResponseEntity<>(response, OK));
    }

    @Async
    @Override
    public CompletableFuture<ResponseEntity<Object>> providePermission(int userId, String permission) throws ExecutionException, InterruptedException {
        User user = findById(userId).get();
        SystemRole userRole = _systemRoleRepository.findByName(permission).orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        Set<SystemRole> roles = new HashSet<>();
        roles.add(userRole);
        user.setSystemRoles(roles);
        _userRepository.save(user);
        return CompletableFuture.completedFuture(ResponseEntity.ok(new
                MessageResponse("UPDATED SUCCESSFULLY!")));
    }

    @Async
    private CompletableFuture<List<UserDTO>> getAllUser(List<User> users, FilterUserDTO filter) throws ExecutionException, InterruptedException {
        formatFilter(filter); /* Remove spaces and make input text become lowercase*/
        users = fetchFilteredUsers(filter, users).get(); /*List of devices after filtering*/
        List<UserDTO> deviceList = convertEntityToDTO(users).get();
        return CompletableFuture.completedFuture(deviceList);
    }

    @Async
    private CompletableFuture<List<UserDTO>> convertEntityToDTO(List<User> users) throws ExecutionException, InterruptedException {
        List<UserDTO> userList = users.stream().map(UserDTO::new).collect(Collectors.toList());
        return CompletableFuture.completedFuture(userList);
    }

    @Async
    private CompletableFuture<Set<String>> selectColumnForKeywordSuggestion(List<UserDTO> userList, String keyword, int fieldColumn) {
        Set<String> keywordList = new HashSet<>();
        Stream<String> mappedDeviceList = null;
        switch (fieldColumn) { /*Fetch only one column*/
            case USER_BADGE_ID_COLUMN -> mappedDeviceList = userList.stream()
                    .map(UserDTO::getBadgeId);
            case USER_NAME_COLUMN -> mappedDeviceList = userList.stream()
                    .map(UserDTO::getUserName);
            case USER_FIRST_NAME_COLUMN -> mappedDeviceList = userList.stream()
                    .map(UserDTO::getFirstName);
            case USER_LAST_NAME_COLUMN -> mappedDeviceList = userList.stream()
                    .map(UserDTO::getLastName);
            case USER_EMAIL_COLUMN -> mappedDeviceList = userList.stream()
                    .map(UserDTO::getEmail);
            case USER_PHONE_NUMBER_COLUMN -> mappedDeviceList = userList.stream()
                    .map(UserDTO::getPhoneNumber);
        }
        if (mappedDeviceList != null) {
            keywordList = mappedDeviceList
                    .filter(element -> element.toLowerCase().contains(keyword.strip().toLowerCase()))
                    .limit(20)
                    .collect(Collectors.toSet());
        }
        return CompletableFuture.completedFuture(keywordList);
    }

    @Async
    private CompletableFuture<List<UserDTO>> getPage(List<UserDTO> sourceList, int pageIndex, int pageSize) {
        if (pageSize <= 0 || pageIndex <= 0) throw new IllegalArgumentException("invalid page size: " + pageSize);

        int fromIndex = (pageIndex - 1) * pageSize;

        if (sourceList == null || sourceList.size() <= fromIndex)
            return CompletableFuture.completedFuture(Collections.emptyList());

        return CompletableFuture.completedFuture(sourceList.subList(fromIndex, Math.min(fromIndex + pageSize, sourceList.size())));
    }

    @Async
    private boolean emailExists(String email) {
        return _userRepository.existsByEmail(email);
    }

    private void formatFilter(FilterUserDTO dto) {
        if (dto.getBadgeId() != null) dto.setBadgeId(dto.getBadgeId().trim().toLowerCase());

        if (dto.getUserName() != null)
            dto.setUserName(dto.getUserName().trim().toLowerCase());

        if (dto.getFirstName() != null)
            dto.setFirstName(dto.getFirstName().trim().toLowerCase());

        if (dto.getLastName() != null) dto.setLastName(dto.getLastName().trim().toLowerCase());

        if (dto.getEmail() != null)
            dto.setEmail(dto.getEmail().trim().toLowerCase());

        if (dto.getPhoneNumber() != null)
            dto.setPhoneNumber(dto.getPhoneNumber().trim().toLowerCase());

        if (dto.getProject() != null)
            dto.setProject(dto.getProject().trim().toLowerCase());

    }

    @Async
    private boolean nameExists(String email) {
        return _userRepository.existsByUserName(email);
    }

    @Async
    private int getTotalPages(int pageSize, int listSize) {
        if (listSize == 0) return 1;

        if (listSize % pageSize == 0) return listSize / pageSize;

        return (listSize / pageSize) + 1;
    }

    private void resendVerificationEmail(User user, String verifyURL) throws MessagingException {
        String toAddress = user.getEmail();
        String subject = "Resend Verification Email";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you!<br>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(FROM_ADDRESS);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getFirstName().concat(" " + user.getLastName()));
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Async
    private CompletableFuture<PasswordResetToken> generateResetPasswordToken(String existingToken) throws ExecutionException, InterruptedException {
        PasswordResetToken rpToken = getResetPasswordToken(existingToken).get();
        rpToken.updateToken(RandomString.make(64));
        _passwordResetTokenRepository.save(rpToken);
        return CompletableFuture.completedFuture(rpToken);
    }

    private void changeUserPassword(User user, String password) {
        user.setPassword(encoder.encode(password));
        _userRepository.save(user);
    }

    private void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken myToken = new PasswordResetToken(token, user);
        _passwordResetTokenRepository.save(myToken);
    }

    private void sendResetPasswordEmail(User user, String verifyURL) throws MessagingException {
        String toAddress = user.getEmail();
        String subject = "Reset password";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to reset your password:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">RESET PASSWORD</a></h3>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
        helper.setFrom(FROM_ADDRESS);
        helper.setTo(toAddress);
        helper.setSubject(subject);
        content = content.replace("[[name]]", user.getFirstName().concat(" " + user.getLastName()));
        content = content.replace("[[URL]]", verifyURL);
        helper.setText(content, true);
        mailSender.send(message);
    }
}
