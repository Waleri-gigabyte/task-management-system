package taskmanagement.security;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import taskmanagement.entity.UserAccount;
import taskmanagement.repository.UsersRepository;

@Service
public class AccountUserDetailsService implements UserDetailsService {

    private final UsersRepository repository;

    public AccountUserDetailsService(UsersRepository repository){
        this.repository = repository;
    }

    @Override
    public UserDetails loadUserByUsername(String email){
        UserAccount account = repository
                .findByEmailIgnoreCase(email)
                .orElseThrow(
                        () ->new UsernameNotFoundException("User not found")
                );
        return User.builder()
                .username(account.getEmail())
                .password(account.getPassword())
                .roles("USER")
                .build();
    }
}

