package mx.edu.utez.sigede_backend.models.credential_field;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialFieldRepository extends JpaRepository<CredentialField, UUID>{
    
}
