package boutique.controllers;

import boutique.config.UserDetailsImpl;
import boutique.models.AuthenticationRequest;
import boutique.models.JwtResponse;
import boutique.models.MessageResponse;
import boutique.models.RegisterRequest;
import boutique.repositories.RoleRepository;
import boutique.repositories.UserRepository;
import boutique.services.UserService;
import boutique.utilities.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/")
public class AuthController {
    AuthenticationManager authenticationManager;
    UserRepository userRepository;
    UserService userService;
    RoleRepository roleRepository;
    PasswordEncoder encoder;
    JwtUtil jwtUtil;

    public AuthController(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          UserService userService,
                          RoleRepository roleRepository,
                          PasswordEncoder encoder,
                          JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody AuthenticationRequest authenticationRequest) {
        if (!this.userRepository.existsByEmail(authenticationRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: No user with this email has been found!"));
        }

        Authentication authentication = this.userService.loginUser(authenticationRequest);

        String jwt = jwtUtil.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getUser().getName(),
                userDetails.getUser().getSurname(),
                userDetails.getUser().getEmail(),
                roles));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        if (this.userRepository.existsByEmail(registerRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }

        if(!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Passwords do not match!"));
        }

        this.userService.registerUser(registerRequest);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
