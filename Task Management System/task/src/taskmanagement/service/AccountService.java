package taskmanagement.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import taskmanagement.entity.UserAccount;
import taskmanagement.repository.UsersRepository;

@Service
public class AccountService {

    private final UsersRepository repository;
    private final PasswordEncoder passwordEncoder;

    public AccountService (
            UsersRepository repository,
            PasswordEncoder passwordEncoder
    ){
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccount save (UserAccount account){
        if (repository.existsByEmailIgnoreCase(account.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
        String encodedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);

        return repository.save(account);
    }
}
