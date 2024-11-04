package mx.edu.utez.sigede_backend.models.verification_code;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import mx.edu.utez.sigede_backend.models.user_account.UserAccount;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "verification_codes")
public class VerificationCode {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "verification_code_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID verificationCodeId;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "expiration", nullable = false)
    private LocalDateTime expiration;

    @ManyToOne
    @JoinColumn(name = "fk_user_account", referencedColumnName = "user_account_id", nullable = false)
    private UserAccount fkUserAccount;

    @PrePersist
    private void ggnerateUUID() {
        if (this.verificationCodeId == null) {
            this.verificationCodeId = UUID.randomUUID();
        }
    }
}
