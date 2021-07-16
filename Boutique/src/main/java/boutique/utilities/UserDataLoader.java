package boutique.utilities;

import boutique.entities.ERole;
import boutique.entities.Role;
import boutique.models.RegisterRequest;
import boutique.repositories.RoleRepository;
import boutique.repositories.UserRepository;
import boutique.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class UserDataLoader implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        loadRoleData();
        loadUserData();
    }

    private void loadUserData() {
        if (userRepository.count() == 0) {
            RegisterRequest requestUser = new RegisterRequest();
            requestUser.setName("User");
            requestUser.setSurname("Test");
            requestUser.setEmail("testuser@email.bg");
            requestUser.setPassword("123456");
            requestUser.setConfirmPassword("123456");

            Set<String> roles = new HashSet<>();
            roles.add(ERole.ROLE_ADMIN.toString());
            RegisterRequest requestAdmin = new RegisterRequest();
            requestAdmin.setName("Admin");
            requestAdmin.setSurname("Test");
            requestAdmin.setEmail("admin@email.bg");
            requestAdmin.setPassword("123456");
            requestAdmin.setConfirmPassword("123456");
            requestAdmin.setRole(roles);

            this.userService.registerUser(requestUser);
            this.userService.registerUser(requestAdmin);
        }
    }

    private void loadRoleData() {
        if (roleRepository.count() == 0) {
            Role user = new Role(ERole.ROLE_USER);
            Role admin = new Role(ERole.ROLE_ADMIN);
            this.roleRepository.saveAndFlush(user);
            this.roleRepository.saveAndFlush(admin);
        }
    }
}
