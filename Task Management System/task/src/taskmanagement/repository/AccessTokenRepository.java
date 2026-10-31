package taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import taskmanagement.entity.AccessToken;

import java.time.Instant;
import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<AccessToken, Long> {

    Optional<AccessToken> findByTokenId (String tokenId);

    @Modifying
    @Query("DELETE FROM AccessToken t WHERE t.expiresAt <= :now")
    int deleteExpiredTokens(@Param("now") Instant now);
}
