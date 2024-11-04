package mx.edu.utez.sigede_backend.models.capturist_profile_field;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CapturistProfileFieldRepository extends JpaRepository<CapturistProfileField, UUID>{
    
}
