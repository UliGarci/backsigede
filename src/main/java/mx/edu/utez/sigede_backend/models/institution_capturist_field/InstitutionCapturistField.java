package mx.edu.utez.sigede_backend.models.institution_capturist_field;

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
import mx.edu.utez.sigede_backend.models.institution.Institution;
import mx.edu.utez.sigede_backend.models.user_info.UserInfo;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "institution_capturist_fields")
public class InstitutionCapturistField {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "institution_capturist_filed_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID institutionCapturistFieldId;

    @Column(name = "is_required", nullable = false, columnDefinition = "BOOLEAN")
    private boolean isRequired;

    @ManyToOne
    @JoinColumn(name = "fk_institution" , referencedColumnName = "institution_id", nullable = false)
    private Institution fkInstitution;

    @ManyToOne
    @JoinColumn(name = "fk_user_info",  referencedColumnName = "user_info_id", nullable = false)
    private UserInfo fkUserInfo;

    @PrePersist
    private void generateUUID(){
        if (this.institutionCapturistFieldId == null) {
            this.institutionCapturistFieldId = UUID.randomUUID();
        }
    }

}
