package boutique.controllers;

import boutique.models.*;
import boutique.repositories.RoleRepository;
import boutique.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
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
}
