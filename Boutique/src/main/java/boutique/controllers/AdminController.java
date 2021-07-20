package boutique.controllers;

import boutique.entities.ERole;
import boutique.entities.Role;
import boutique.entities.User;
import boutique.models.*;
import boutique.repositories.RoleRepository;
import boutique.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder encoder;

    public AdminController(UserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
    }

    @GetMapping()
    public ResponseEntity<?> listUsers() {
        try {
            List<UserResponse> users = this.userRepository.findAll()
                    .stream().map(x -> new UserResponse(
                            x.getId(),
                            x.getName(),
                            x.getSurname(),
                            x.getAddress(),
                            x.getEmail(),
                            x.getRoles().stream().map(r -> new RoleResponse(
                                    r.getId(), r.getName())).collect(Collectors.toSet())
                    )).collect(Collectors.toList());

            return ResponseEntity.ok(users);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not load users.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Integer id) {
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
                    user.getEmail(),
                    user.getRoles().stream().map(x -> new RoleResponse(
                            x.getId(), x.getName()
                    )).collect(Collectors.toSet()));

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
    public ResponseEntity<?> edit(@PathVariable Integer id) {
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
                    userEntity.getEmail(),
                    userEntity.getRoles().stream().map(x -> new RoleResponse(
                            x.getId(), x.getName()
                    )).collect(Collectors.toSet()));

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
    public ResponseEntity<?> editProcess(@PathVariable Integer id, @RequestBody UserRequest userRequest) {
        try {
            if (!this.userRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find user with id ${id}."));
            }

            if(!userRequest.getPassword().equals(userRequest.getConfirmPassword())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Passwords do not match!"));
            }

            User user = this.userRepository.findById(id).get();
            user.setName(userRequest.getName());
            user.setSurname(userRequest.getSurname());
            user.setAddress(userRequest.getAddress());
            user.setEmail(userRequest.getEmail());
            user.setPassword(encoder.encode(userRequest.getPassword()));

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
                            Role userRole = this.roleRepository.findByName(ERole.ROLE_USER)
                                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                            roles.add(userRole);
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
    public ResponseEntity<?> delete(@PathVariable Integer id) {
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
                    userEntity.getEmail(),
                    userEntity.getRoles().stream().map(x -> new RoleResponse(
                            x.getId(), x.getName()
                    )).collect(Collectors.toSet()));

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
    public ResponseEntity<?> deleteProcess(@PathVariable Integer id) {
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
}
