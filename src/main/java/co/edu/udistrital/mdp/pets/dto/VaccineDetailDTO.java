package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.VaccineEntryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VaccineDetailDTO extends VaccineDTO {
    // Detailed view: currently no additional nested objects beyond base DTO.
    // Extend when full MedicalHistory or linked Pet data is needed.

    public VaccineDetailDTO(VaccineEntryEntity entity) {
        super(entity);
    }
}
