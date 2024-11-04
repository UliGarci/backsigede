package mx.edu.utez.sigede_backend.models.credential_field;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.credential.Credential;
import mx.edu.utez.sigede_backend.models.user_info.UserInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credential_fields")
public class CredentialField {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "credential_field_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID credentialFieldId;

    @Column(name = "value", columnDefinition = "VARBINARY(255)", nullable = false)
    private byte[] value;

    @ManyToOne
    @JoinColumn(name = "fk_credential", referencedColumnName = "credential_id", nullable = false)
    private  Credential fkCredential;

    @ManyToOne
    @JoinColumn(name = "fk_user_info", referencedColumnName = "user_info_id", nullable = false)
    private UserInfo fkUserInfo;

    @PrePersist
    private void generateUUID() {
        if (this.credentialFieldId == null) {
            this.credentialFieldId = UUID.randomUUID();
        }
    }
}
