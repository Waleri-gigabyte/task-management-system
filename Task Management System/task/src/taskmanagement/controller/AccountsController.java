package taskmanagement.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import taskmanagement.entity.UserAccount;
import taskmanagement.service.AccountService;

@RestController
public class AccountsController {

    private final AccountService service;

    public AccountsController (AccountService service){
        this.service = service;
    }

    @PostMapping("/api/accounts")
    public ResponseEntity<Void> add (
            @Valid @RequestBody UserAccount account
            ){
        service.save(account);
        return ResponseEntity.ok().build();
    }
}
