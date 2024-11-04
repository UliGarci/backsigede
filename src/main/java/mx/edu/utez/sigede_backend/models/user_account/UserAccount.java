package mx.edu.utez.sigede_backend.models.user_account;

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
import mx.edu.utez.sigede_backend.models.rol.Rol;
import mx.edu.utez.sigede_backend.models.status.Status;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user_accocunts")
public class UserAccount {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "user_account_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID userAccountId;

    @Column(name = "email", nullable = false, unique = true, columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(name = "password", nullable = false, columnDefinition = "TEXT")
    private String password;

    @Column(name = "name", nullable = false, columnDefinition = "VARCHAR(255)")
    private String name;

    @ManyToOne
    @JoinColumn(name = "fk_rol", referencedColumnName = "rol_id", nullable = false, columnDefinition = "BINARY(16)")
    private Rol fkRol;

    @ManyToOne
    @JoinColumn(name = "fk_status", referencedColumnName = "status_id", nullable = false, columnDefinition = "BINARY(16)")
    private Status fkStatus;

    @PrePersist
    private void generateUUID() {
        if (this.userAccountId == null) {
            this.userAccountId = UUID.randomUUID();
        }
    }
}
