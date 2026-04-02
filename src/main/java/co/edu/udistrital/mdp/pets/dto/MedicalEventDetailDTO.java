package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MedicalEventDetailDTO extends MedicalEventDTO {
    // Detailed view: currently no additional nested objects beyond the base DTO.
    // This class can be extended when richer associations are required.

    public MedicalEventDetailDTO(MedicalEventEntity entity) {
        super(entity);
    }
}
