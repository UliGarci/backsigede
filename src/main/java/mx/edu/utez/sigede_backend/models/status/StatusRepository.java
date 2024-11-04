package mx.edu.utez.sigede_backend.models.status;

import  java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, UUID> {
    
}
