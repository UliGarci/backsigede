package mx.edu.utez.sigede_backend.models.user_account;

import java.util.UUID;

import org.apache.el.stream.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
    // @Query("select usac from UserAccount as usac where usac.email = :email")
    // Optional<UserAccount> getOneByEmail(@Param("email") String encryptedEmail);
    boolean existsByUserAccountId(UUID userAccountId);
    UserAccount findByUserAccountId(UUID userAccountId);
    boolean existsByEmail(String email);
    UserAccount findByEmail(String email);
}
