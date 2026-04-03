package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MedicalHistoryDTO {
    private Long id;
    private LocalDate createdDate;
    private LocalDate lastUpdated;
    private Long petId;
    private Long veterinarianId;

    public MedicalHistoryDTO(MedicalHistoryEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.createdDate = entity.getCreatedDate();
            this.lastUpdated = entity.getLastUpdated();
            if (entity.getPet() != null) {
                this.petId = entity.getPet().getId();
            }
            if (entity.getVeterinarian() != null) {
                this.veterinarianId = entity.getVeterinarian().getId();
            }
        }
    }
}
