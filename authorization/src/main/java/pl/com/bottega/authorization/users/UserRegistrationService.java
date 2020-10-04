package pl.com.bottega.authorization.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public void registerUser(RegisterUserCommand command) {
        var user = new User();
        var userRole = new UserRole();
        userRole.setRole(Role.USER);
        user.getRoles().add(userRole);
        user.setEmployeeId(command.getEmployeeId());
        user.setPassword(passwordEncoder.encode(command.getPassword()));
        user.setUsername(command.getUsername());
        userRole.setUser(user);

        userRepository.save(user);
    }
}
