package mx.edu.utez.sigede_backend.models.credential;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "credentials")
public class Credential {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "credential_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID credentialId;

    @Column(name = "issue_date", nullable = false, columnDefinition = "TIMESTAMP")
    private LocalDateTime issueDate;

    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Lob
    @Column(name = "user_photo", nullable = false, columnDefinition = "LONGBLOB")
    private byte[] userPhoto;

    @ManyToOne
    @JoinColumn(name = "fk_institution", referencedColumnName = "institution_id", nullable = false)
    private Institution fkInstitution;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_user_account", referencedColumnName = "user_account_id", nullable = false, unique = true)
    private UserAccount fkUserAccount;

    @PrePersist
    private void generateUUID() {
        if (this.credentialId == null) {
            this.credentialId = UUID.randomUUID();
        }
    }
}
