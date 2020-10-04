package pl.com.bottega.authorization.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.com.bottega.authorization.users.UserRepository;

import java.util.stream.Collectors;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Auth failed"));
        if(user == null) {
            return User.builder().build();
        } else {
            return User
                    .withUsername(username)
                    .password(user.getPassword())
                    .roles(user.getRoles().stream()
                            .map(userRole -> userRole.getRole().toString())
                            .collect(Collectors.toList()).toArray(new String[]{})
                    ).build();
        }
    }
}
