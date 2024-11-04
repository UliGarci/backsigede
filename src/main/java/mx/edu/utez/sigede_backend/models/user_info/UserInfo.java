package mx.edu.utez.sigede_backend.models.user_info;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.util.Map;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users_info")
public class UserInfo {
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Id
    @GeneratedValue(generator = "UUID")
    @Column(name = "user_info_id", columnDefinition = "BINARY(16)", nullable = false)
    private UUID userInfoId;

    @Column(name = "tag", columnDefinition = "VARCHAR(255)", nullable = false)
    private String tag;

    @Column(name = "type", columnDefinition = "VARCHAR(100)", nullable = false)
    private String type;

    @Column(name = "is_in_qr", columnDefinition = "BOOLEAN", nullable = false)
    private boolean isInQr;

    @Column(name = "is_in_card", columnDefinition = "BOOLEAN", nullable = false)
    private boolean isInCard;

    @Lob
    @Column(name = "options", columnDefinition = "JSON", nullable = true)
    private String options;

    // Convertir el JSON de options a un Map en tiempo de ejecucición
    public Map<String, Object> getOptionsAsMap() {
        try{
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(this.options, new TypeReference<Map<String, Object>>(){});
        } catch (Exception e) {
            throw new RuntimeException("Error al converitr JSON a Map", e);
        }
    }

    // Parsear options desde el Map
    public void setOptionsFromMap(Map<String, Object> optionsMap) {
        try{
            ObjectMapper mapper = new ObjectMapper();
            this.options = mapper.writeValueAsString(optionsMap);
        } catch (Exception e) {
            throw new RuntimeException("Error al converitr Map a JSON", e);
        }
    }

    @PrePersist
    private void generateUUID() {
        if (this.userInfoId == null) {
            this.userInfoId = UUID.randomUUID();
        }
    }
}
