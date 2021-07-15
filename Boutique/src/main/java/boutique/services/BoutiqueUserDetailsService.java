package boutique.services;

import boutique.config.UserDetailsImpl;
import boutique.entities.User;
import boutique.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class BoutiqueUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public BoutiqueUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = userRepository.findByEmail(email);
            return UserDetailsImpl.build(user);
        }
        catch (UsernameNotFoundException ex) {
            throw new UsernameNotFoundException("User Not Found with email: " + email);
        }
    }
}
