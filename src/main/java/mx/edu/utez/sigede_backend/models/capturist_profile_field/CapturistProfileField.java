package mx.edu.utez.sigede_backend.models.capturist_profile_field;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.capturist_profile.CapturistProfile;
import mx.edu.utez.sigede_backend.models.user_info.UserInfo; 

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "capturist_profile_fields")
public class CapturistProfileField {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "capturist_profile_field_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID capturistProfileFieldId;

    @Column(name = "value", columnDefinition = "VARCHAR(255)", nullable = false)
    private String value;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_profile", referencedColumnName = "capturist_profile_id", nullable = false)
    private CapturistProfile fkProfile;

    @ManyToOne
    @JoinColumn(name = "fk_user_info", referencedColumnName = "user_info_id", nullable = false)
    private UserInfo fkUserInfo;

    @PrePersist
    private void generateUUID() {
        if(this.capturistProfileFieldId == null){
            this.capturistProfileFieldId = UUID.randomUUID();
        }
    }
}
