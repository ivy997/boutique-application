package boutique.controllers;

import boutique.config.UserDetailsImpl;
import boutique.entities.ERole;
import boutique.entities.Role;
import boutique.entities.User;
import boutique.models.MessageResponse;
import boutique.models.RoleResponse;
import boutique.models.UserRequest;
import boutique.models.UserResponse;
import boutique.repositories.RoleRepository;
import boutique.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/profile")
@PreAuthorize("hasRole('ADMIN') or #userDetails.user.id == #id")
public class ProfileController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;

    public ProfileController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Integer id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            if (!this.userRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find user with id ${id}."));
            }

            User user = this.userRepository.findById(id).get();
            UserResponse userResponse = new UserResponse(
                    user.getId(),
                    user.getName(),
                    user.getSurname(),
                    user.getAddress(),
                    user.getEmail());

            if (checkIfUserIsAdmin(userDetails)) {
                userResponse.setRoles(user.getRoles().stream().map(x -> new RoleResponse(
                        x.getId(), x.getName()
                )).collect(Collectors.toSet()));
            }

            return ResponseEntity.ok(userResponse);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not get user details.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @GetMapping("/edit/{id}")
    public ResponseEntity<?> edit(@PathVariable Integer id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            if (!this.userRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find user with id ${id}."));
            }

            User userEntity = this.userRepository.findById(id).get();
            UserResponse userResponse = new UserResponse(
                    userEntity.getId(),
                    userEntity.getName(),
                    userEntity.getSurname(),
                    userEntity.getAddress(),
                    userEntity.getEmail());

            if (checkIfUserIsAdmin(userDetails)) {
                userResponse.setRoles(userEntity.getRoles().stream().map(x -> new RoleResponse(
                        x.getId(), x.getName()
                )).collect(Collectors.toSet()));
            }

            return ResponseEntity.ok(userResponse);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not load user.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @PostMapping("/edit/{id}")
    public ResponseEntity<?> editProcess(@PathVariable Integer id,
                                         @RequestBody UserRequest userRequest,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            if (!this.userRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find user with id ${id}."));
            }

            User user = this.userRepository.findById(id).get();

            if (userRequest.getPassword() != null) {
                if(!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
                    return ResponseEntity
                            .badRequest()
                            .body(new MessageResponse("Error: Passwords do not match!"));
                }

                user.setPassword(encoder.encode(userRequest.getPassword()));
            }

            if (userRequest.getName() != null) {
                user.setName(userRequest.getName());
            }
            if (userRequest.getSurname() != null) {
                user.setSurname(userRequest.getSurname());
            }
            if (userRequest.getAddress() != null) {
                user.setAddress(userRequest.getAddress());
            }
            if (userRequest.getEmail() != null) {
                user.setEmail(userRequest.getEmail());
            }

            Set<Role> roles = new HashSet<>();
            if (userRequest.getRoles() == null) {
                Role userRole = this.roleRepository.findByName(ERole.ROLE_USER)
                        .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                roles.add(userRole);
            }
            else {
                userRequest.getRoles().forEach(role -> {
                    switch (role) {
                        case "admin":
                            Role adminRole = this.roleRepository.findByName(ERole.ROLE_ADMIN)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(adminRole);

                            break;
                        default:
                            Role defaultUserRole = this.roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(defaultUserRole);
                    }
                });
            }

            user.setRoles(roles);

            this.userRepository.saveAndFlush(user);

            return ResponseEntity.ok(new MessageResponse("User edited successfully!"));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not edit user.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @GetMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            if (!this.userRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find user with id ${id}."));
            }

            User userEntity = this.userRepository.findById(id).get();
            UserResponse userResponse = new UserResponse(
                    userEntity.getId(),
                    userEntity.getName(),
                    userEntity.getSurname(),
                    userEntity.getAddress(),
                    userEntity.getEmail());

            if (checkIfUserIsAdmin(userDetails)) {
                userResponse.setRoles(userEntity.getRoles().stream().map(x -> new RoleResponse(
                        x.getId(), x.getName()
                )).collect(Collectors.toSet()));
            }

            return ResponseEntity.ok(userResponse);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not load user.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<?> deleteProcess(@PathVariable Integer id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            if (!this.userRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find user with id ${id}."));
            }

            User user = this.userRepository.findById(id).get();

            this.userRepository.delete(user);

            return ResponseEntity.ok(new MessageResponse("User deleted successfully!"));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not delete user.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    private boolean checkIfUserIsAdmin(UserDetailsImpl userDetails) {
        Set<String> roles = new HashSet<>();
        for (GrantedAuthority role : userDetails.getAuthorities()) {
            roles.add(role.getAuthority());
        }

        if (roles.contains(ERole.ROLE_ADMIN.toString())) {
            return true;
        }

        return false;
    }
}
