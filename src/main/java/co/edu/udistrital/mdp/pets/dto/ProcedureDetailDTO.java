package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.ProcedureEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Detail DTO for veterinary Procedures.
 * Extends ProcedureDTO with additional nested information when available.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ProcedureDetailDTO extends ProcedureDTO {
    // Extend with nested objects (e.g., MedicalHistoryDTO, VeterinarianDTO) once entity is created.
    public ProcedureDetailDTO(ProcedureEntity entity) {
        super(entity);
    }
}
