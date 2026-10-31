package taskmanagement.service;

import jakarta.transaction.Transactional;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.stereotype.Service;
import taskmanagement.entity.AccessToken;
import taskmanagement.entity.UserAccount;
import taskmanagement.repository.AccessTokenRepository;
import taskmanagement.repository.UsersRepository;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;

import java.time.Instant;
import java.time.Duration;
import java.util.UUID;

@Service
public class TokenService {

    private final AccessTokenRepository tokenRepository;
    private final UsersRepository usersRepository;
    private final JwtEncoder jwtEncoder;

    public TokenService (
            AccessTokenRepository tokenRepository,
            UsersRepository usersRepository,
            JwtEncoder jwtEncoder
    ){
        this.tokenRepository = tokenRepository;
        this.usersRepository = usersRepository;
        this.jwtEncoder = jwtEncoder;
    }

    @Transactional
    public String generateToken (Authentication authentication) {
        String email = authentication.getName();

        UserAccount user = usersRepository
                .findByEmailIgnoreCase(email)
                .orElseThrow();

        String tokenId = UUID.randomUUID().toString();

        Instant createdAt = Instant.now();
        Instant expiresAt = createdAt.plus(Duration.ofHours(1));

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(user.getId().toString())
                .id(tokenId)
                .issuedAt(createdAt)
                .expiresAt(expiresAt)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS256).build();

        String jwt = jwtEncoder
                .encode(JwtEncoderParameters.from(header,claims))
                .getTokenValue();

        AccessToken accessToken = new AccessToken();

        accessToken.setTokenId(tokenId);
        accessToken.setUser(user);
        accessToken.setCreatedAt(createdAt);
        accessToken.setExpiresAt(expiresAt);
        accessToken.setRevoked(false);

        tokenRepository.save(accessToken);

        return jwt;
    }

    @Scheduled(fixedRate = 60_000)
    @Transactional
    public void deleteExpiredTokens(){
        int deleted = tokenRepository.deleteExpiredTokens(Instant.now());
    }
}
