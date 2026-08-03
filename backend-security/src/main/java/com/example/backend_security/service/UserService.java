package com.example.backend_security.service;

import com.example.backend_security.constants.AuthConstants;

import com.example.backend_security.constants.RolesConstants;
import com.example.backend_security.constants.StatusConstants;
import com.example.backend_security.dto.*;
import com.example.backend_security.entity.Role;
import com.example.backend_security.entity.User;
import com.example.backend_security.entity.UserStatus;
import com.example.backend_security.exception.ResourceAlreadyExistsException;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.mapper.UserMapper;
import com.example.backend_security.repository.RoleRepository;
import com.example.backend_security.repository.UserRepository;
import com.example.backend_security.repository.UserStatusRepository;
import com.example.backend_security.security.JwtUtil;
import com.example.backend_security.util.JwtCookieUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserStatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtils;
    private final TokenService tokenService;
    private final JwtCookieUtil jwtCookieUtil;
    private final UserMapper userMapper;

    public User createUser(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        Role defaultRole = roleRepository.findByName(RolesConstants.ADMIN)
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        UserStatus defaultStatus = statusRepository.findByCode(StatusConstants.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Default status not found"));

        User newUser = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .provider("LOCAL")
                .password(passwordEncoder.encode(request.getPassword()))
                .role(defaultRole)
                .status(defaultStatus)
                .creationDate(LocalDateTime.now())
                .build();


        return userRepository.save(newUser);
    }


    public User registerOrUpdateOAuthUser(Map<String, Object> userInfo) throws Exception {
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String photoUrl = (String) userInfo.get("picture");

        return userRepository.findByEmail(email).map(user -> {
            user.setName(name);
            user.setPhotoUrl(photoUrl);
            return userRepository.save(user);
        }).orElseGet(() -> {

                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setPhotoUrl(photoUrl);
                newUser.setProvider("google");
                newUser.setCreationDate(LocalDateTime.now());

                Role defaultRole = roleRepository.findByName(RolesConstants.USER)
                        .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
                newUser.setRole(defaultRole);

                UserStatus defaultStatus = statusRepository.findByCode(StatusConstants.ACTIVE)
                        .orElseThrow(() -> new ResourceNotFoundException("Default status not found"));
                newUser.setStatus(defaultStatus);
                return userRepository.save(newUser);
        });
    }

    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado"));

        return userMapper.toResponse(user);
    }

    public UserResponse getUserByUsername(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toResponse(user);
    }

    public UserResponse getUserByEmail(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado"));
        return userMapper.toResponse(user);
    }

    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public User updateUser(Long userId, RegisterRequest updatedUser) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + userId + " not found"));

        if (!user.getUsername().equals(updatedUser.getUsername()) && userRepository.existsByUsername(updatedUser.getUsername())) {
            throw new ResourceAlreadyExistsException("El nombre de usuario ya existe");
        }

        if (!user.getEmail().equals(updatedUser.getEmail()) && userRepository.existsByEmail(updatedUser.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists");
        }

        user.setName(updatedUser.getName());
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(user);
    }


    public TokenResponse login(LoginRequest loginRequest, HttpServletResponse response) {

        String identificador = loginRequest.getLogin();
        String password = loginRequest.getPassword();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(identificador, password));

        User user = userRepository.findByUsername(identificador)
                .orElseGet(() -> userRepository.findByEmail(identificador)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Usuario no encontrado")));

        String token = jwtUtils.generateToken(user);
        tokenService.create(user.getId(), token);


        jwtCookieUtil.saveToken(response, token);

        return TokenResponse.builder()
                .message("Login correcto")
                .token(token)
                .user(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().getName())
                .expiration("7 días")
                .build();
    }

    public User actualUsuario(Principal principal) {
        return userRepository.findByUsername(principal.getName())
                .orElseGet(() -> userRepository.findByEmail(principal.getName())
                        .orElseThrow(() -> new UsernameNotFoundException(AuthConstants.USUARIO_NO_VALIDO + principal.getName())));
    }


    public List<UserResponse> getActiveUsers() {

        return userRepository.findByStatus_Code(StatusConstants.ACTIVE)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }


    public List<UserResponse> getInactiveUsers() {

        return userRepository.findByStatus_Code(StatusConstants.INACTIVE)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }


    public List<UserResponse> getSuspendUsers() {

        return userRepository.findByStatus_Code(StatusConstants.SUSPEND)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }


    public List<UserResponse> getBlockedUsers() {

        return userRepository.findByStatus_Code(StatusConstants.BLOCKED)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public List<UserResponse> getUsersByRoleUser() {

        return userRepository.findByRole_Name(RolesConstants.USER)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }


    public List<UserResponse> getUsersByRoleAdmin() {

        return userRepository.findByRole_Name(RolesConstants.ADMIN)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public List<UserResponse> getActiveUsersByRoleUser() {

        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.USER, StatusConstants.ACTIVE)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public List<UserResponse> getSuspendedUsersByRoleUser() {

        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.USER, StatusConstants.SUSPEND)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public List<UserResponse> getInactiveUsersByRoleUser() {

        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.USER, StatusConstants.INACTIVE)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public List<UserResponse> getBlockedUsersByRoleUser() {

        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.USER, StatusConstants.BLOCKED)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public List<UserResponse> getActiveUsersByRoleAdmin() {

        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.ADMIN, StatusConstants.ACTIVE)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public List<UserResponse> getSuspendedUsersByRoleAdmin() {

        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.ADMIN, StatusConstants.SUSPEND)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public List<UserResponse> getInactiveUsersByRoleAdmin() {

        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.ADMIN, StatusConstants.INACTIVE)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }


    public List<UserResponse> getBlockedUsersByRoleAdmin() {

        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.ADMIN, StatusConstants.BLOCKED)
                .stream()
                .map(userMapper::toResponse)
                .toList();
    }

    public UserResponse inactiveUser(Long id) {
        return userMapper.toResponse(changeStatus(id, StatusConstants.INACTIVE));
    }


    public UserResponse activeUser(Long id) {
        return userMapper.toResponse(changeStatus(id, StatusConstants.ACTIVE));
    }

    public UserResponse suspendUser(Long id) {
        return userMapper.toResponse(changeStatus(id, StatusConstants.SUSPEND));
    }

    public UserResponse blockedUser(Long id) {
        return userMapper.toResponse(changeStatus(id, StatusConstants.BLOCKED));
    }

    private User changeStatus(Long id, String statusCode) {

        User user = userRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Usuario no encontrado con código: " + id));

        UserStatus status = statusRepository.findByCode(statusCode)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Estado no encontrado: " + statusCode));
        user.setStatus(status);
        return userRepository.save(user);
    }

    public List<UserStatusPercentageDTO> getStatusPercentages() {
        return userRepository.getUserStatusPercentages();
    }
}
