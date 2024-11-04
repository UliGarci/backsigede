package mx.edu.utez.sigede_backend.models.credential;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, UUID>{
    
}
