package com.example.backend_security.service;

import com.example.backend_security.constants.AuthConstants;

import com.example.backend_security.constants.RolesConstants;
import com.example.backend_security.constants.StatusConstants;
import com.example.backend_security.dto.LoginRequest;
import com.example.backend_security.dto.RegisterRequest;
import com.example.backend_security.dto.TokenResponse;
import com.example.backend_security.dto.UserStatusPercentageDTO;
import com.example.backend_security.entity.Role;
import com.example.backend_security.entity.User;
import com.example.backend_security.entity.UserStatus;
import com.example.backend_security.exception.BadRequestException;
import com.example.backend_security.exception.JwtAuthenticationException;
import com.example.backend_security.exception.ResourceAlreadyExistsException;
import com.example.backend_security.exception.ResourceNotFoundException;
import com.example.backend_security.repository.RoleRepository;
import com.example.backend_security.repository.UserRepository;
import com.example.backend_security.repository.UserStatusRepository;
import com.example.backend_security.security.JwtUtil;
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


    public User createUser(RegisterRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResourceAlreadyExistsException("Username already exists: " + request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResourceAlreadyExistsException("Email already exists: " + request.getEmail());
        }

        Role defaultRole = roleRepository.findByName(RolesConstants.USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        UserStatus defaultStatus = statusRepository.findByCode(StatusConstants.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Default status not found"));

        User newUser = User.builder()
                .name(request.getName())
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(defaultRole)
                .status(defaultStatus)
                .creationDate(LocalDateTime.now())
                .build();


        return userRepository.save(newUser);
    }

    // =========================
    // Crear un google
    // =========================
    public User registerOrUpdateOAuthUser(Map<String, Object> userInfo) throws Exception {
        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");
        String photoUrl = (String) userInfo.get("picture");

        // Buscar si el usuario ya existe
        return userRepository.findByEmail(email).map(user -> {
            // Actualiza los campos si el usuario ya existe
            user.setName(name);
            user.setPhotoUrl(photoUrl);
            return userRepository.save(user);
        }).orElseGet(() -> {
            try {
                User newUser = new User();
                newUser.setEmail(email);
                newUser.setName(name);
                newUser.setPhotoUrl(photoUrl);
                newUser.setProvider("google");
                newUser.setCreationDate(LocalDateTime.now());


                // Asignar Role por defecto
                Role defaultRole = roleRepository.findByName(RolesConstants.USER)
                        .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));
                newUser.setRole(defaultRole);

                // Asignar Status por defecto
                UserStatus defaultStatus = statusRepository.findByCode(StatusConstants.ACTIVE)
                        .orElseThrow(() -> new ResourceNotFoundException("Default status not found"));
                newUser.setStatus(defaultStatus);

                return userRepository.save(newUser);
            } catch (Exception e) {
                throw new BadRequestException(e.getMessage());
            }
        });
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
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

        // Actualizar campos
        user.setName(updatedUser.getName());
        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(user);
    }



    public TokenResponse login(LoginRequest loginRequest) {
        String identificador = loginRequest.getLogin();
        String password = loginRequest.getPassword();

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(identificador, password)
            );


            User user = userRepository.findByUsername(identificador)
                    .orElseGet(() -> userRepository.findByEmail(identificador)
                            .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado: " + identificador)));


            String token = jwtUtils.generateToken(user);
            tokenService.createToken(user.getId(), token);

            return new TokenResponse(token);

        } catch (BadCredentialsException ex) {
            // Usuario o contraseña incorrecta → 401
            throw new JwtAuthenticationException(AuthConstants.USER_PASS_INCORRECTO);
        } catch (Exception ex) {
            // Error genérico en login → 400 o 500 según contexto
            throw new BadRequestException(AuthConstants.ERROR_LOGIN);
        }
    }

    public User actualUsuario(Principal principal) {
        if (principal == null || principal.getName() == null) {
            throw new RuntimeException(AuthConstants.USUARIO_NO_AUTORIZADO);
        }

        User user = userRepository.findByUsername(principal.getName())
                .orElseGet(() -> userRepository.findByEmail(principal.getName())
                        .orElseThrow(() -> new UsernameNotFoundException(AuthConstants.USUARIO_NO_VALIDO + principal.getName())));
        return user;
    }



    public List<User> getActiveUsers() {
        return userRepository.findByStatus_Code(StatusConstants.ACTIVE);
    }

    public List<User> getInactiveUsers() {
        return userRepository.findByStatus_Code(StatusConstants.INACTIVE);
    }

    public List<User> getSuspendUsers() {
        return userRepository.findByStatus_Code(StatusConstants.SUSPEND);
    }

    public List<User> getBlockedUsers() {
        return userRepository.findByStatus_Code(StatusConstants.BLOCKED);
    }

    public List<User> getUsersByRoleUser() {
        return userRepository.findByRole_Name(RolesConstants.USER);
    }


    public List<User> getUsersByRoleAdmin() {
        return userRepository.findByRole_Name(RolesConstants.ADMIN);
    }

    public List<User> getActiveUsersByRoleUser() {
        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.USER, StatusConstants.ACTIVE);
    }

    public List<User> getSuspendedUsersByRoleUser() {
        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.USER, StatusConstants.SUSPEND);
    }

    public List<User> getInactiveUsersByRoleUser() {
        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.USER, StatusConstants.INACTIVE);
    }

    public List<User> getBlockedUsersByRoleUser() {
        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.USER, StatusConstants.BLOCKED);
    }

    public List<User> getActiveUsersByRoleAdmin() {
        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.ADMIN, StatusConstants.ACTIVE);
    }

    public List<User> getSuspendedUsersByRoleAdmin() {
        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.ADMIN, StatusConstants.SUSPEND);
    }

    public List<User> getInactiveUsersByRoleAdmin() {
        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.ADMIN, StatusConstants.INACTIVE);
    }

    public List<User> getBlockedUsersByRoleAdmin() {
        return userRepository.findByRole_NameAndStatus_Code(RolesConstants.ADMIN, StatusConstants.BLOCKED);
    }


    public User InactiveUser(Long codigo) {
        User user = userRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con código: " + codigo));

        UserStatus inactiveStatus = statusRepository.findByCode(StatusConstants.INACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Estado INACTIVE no encontrado"));

        user.setStatus(inactiveStatus);

        return userRepository.save(user);
    }

    public User ActiveUser(Long codigo) {
        User user = userRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con código: " + codigo));

        UserStatus inactiveStatus = statusRepository.findByCode(StatusConstants.ACTIVE)
                .orElseThrow(() -> new ResourceNotFoundException("Estado ACTIVE no encontrado"));

        user.setStatus(inactiveStatus);

        return userRepository.save(user);
    }

    public User BlockedUser(Long codigo) {
        User user = userRepository.findById(codigo)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con código: " + codigo));

        UserStatus inactiveStatus = statusRepository.findByCode(StatusConstants.BLOCKED)
                .orElseThrow(() -> new ResourceNotFoundException("Estado BLOCKED no encontrado"));

        user.setStatus(inactiveStatus);

        return userRepository.save(user);
    }


    public User SuspendUser(Long código) {
        User user = userRepository.findById(código)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con código: " + código));

        UserStatus inactiveStatus = statusRepository.findByCode(StatusConstants.SUSPEND)
                .orElseThrow(() -> new ResourceNotFoundException("Estado SUSPEND no encontrado"));

        user.setStatus(inactiveStatus);

        return userRepository.save(user);
    }
    public List<UserStatusPercentageDTO> getStatusPercentages() {
        return userRepository.getUserStatusPercentages();
    }
}
