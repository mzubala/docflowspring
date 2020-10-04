package pl.com.bottega.authorization.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.authorization.users.RegisterUserCommand;
import pl.com.bottega.authorization.users.UserRegistrationService;

@RestController
@RequestMapping("/registration")
public class RegisterUserController {

    @Autowired
    private UserRegistrationService service;

    @PostMapping
    public void registerUser(@RequestBody RegisterUserCommand registerUserCommand) {
        service.registerUser(registerUserCommand);
    }
}
