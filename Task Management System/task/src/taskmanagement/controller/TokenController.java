package taskmanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import taskmanagement.dto.TokenResponse;
import taskmanagement.service.TokenService;

@Controller
@RequestMapping("/api/auth/token")
public class TokenController {


    private  final TokenService service;

    public TokenController (TokenService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<TokenResponse> post (
            Authentication authentication
    ) {
        String token = service.generateToken(authentication);

        return ResponseEntity.ok(new TokenResponse(token));
    }
}
