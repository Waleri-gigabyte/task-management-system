package taskmanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import taskmanagement.entity.UserAccount;

import java.util.Optional;

@Repository
public interface UsersRepository extends JpaRepository <UserAccount, Long> {

    boolean existsByEmailIgnoreCase(String email);

    Optional<UserAccount> findByEmailIgnoreCase(String email);
}
