package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MedicalEventDTO {
    private Long id;
    private String eventType;
    private String description;
    private LocalDate eventDate;
    private Long medicalHistoryId;
    private Long veterinarianId;

    public MedicalEventDTO(MedicalEventEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.eventType = entity.getEventType();
            this.description = entity.getDescription();
            this.eventDate = entity.getEventDate();
            if (entity.getMedicalHistory() != null) {
                this.medicalHistoryId = entity.getMedicalHistory().getId();
            }
            if (entity.getVeterinarian() != null) {
                this.veterinarianId = entity.getVeterinarian().getId();
            }
        }
    }

    public MedicalEventEntity toEntity() {
        MedicalEventEntity entity = new MedicalEventEntity();
        entity.setId(this.id);
        entity.setEventType(this.eventType);
        entity.setDescription(this.description);
        entity.setEventDate(this.eventDate);
        return entity;
    }
}
