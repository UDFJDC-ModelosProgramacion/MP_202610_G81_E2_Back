package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Detail DTO for an Animal, including associated medical history and adoption info.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AnimalDetailDTO extends AnimalDTO {
    private MedicalHistoryDTO medicalHistory;
    private Long shelterId;

    public AnimalDetailDTO(PetEntity entity) {
        super(entity);
        if (entity != null) {
            if (entity.getMedicalHistory() != null) {
                this.medicalHistory = new MedicalHistoryDTO(entity.getMedicalHistory());
            }
            if (entity.getShelter() != null) {
                this.shelterId = entity.getShelter().getId();
            }
        }
    }
}
