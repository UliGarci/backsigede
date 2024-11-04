package mx.edu.utez.sigede_backend.models.capturist_profile;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;


import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "capturist_profiles")
public class CapturistProfile {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "capturist_profile_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID capturistProfileId;    

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_profile",  referencedColumnName = "user_account_id")
    private UserAccount fkProfile;

    @PrePersist
    private void generateUUID() {
        if (this.capturistProfileId == null) {
            this.capturistProfileId = UUID.randomUUID();
        }
    }

}

