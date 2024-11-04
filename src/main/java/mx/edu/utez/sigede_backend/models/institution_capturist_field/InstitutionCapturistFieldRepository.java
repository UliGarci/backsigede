package mx.edu.utez.sigede_backend.models.institution_capturist_field;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InstitutionCapturistFieldRepository extends JpaRepository<InstitutionCapturistField, UUID> {
    
}
