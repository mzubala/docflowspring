package pl.com.bottega.authorization.users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UserRegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void registerUser(RegisterUserCommand command) {

    }
}
