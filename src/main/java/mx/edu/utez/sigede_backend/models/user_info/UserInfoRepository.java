package mx.edu.utez.sigede_backend.models.user_info;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserInfoRepository extends JpaRepository<UserInfo, UUID>{
    
}
