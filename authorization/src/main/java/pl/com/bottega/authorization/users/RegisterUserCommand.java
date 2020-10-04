package pl.com.bottega.authorization.users;

import lombok.Data;

@Data
public class RegisterUserCommand {
    private String username;
    private String password;
    private Long employeeId;
}
