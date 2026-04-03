package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Detail DTO for Infection events.
 * Extends InfectionDTO with additional nested information when available.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class InfectionDetailDTO extends InfectionDTO {
    // Extend with nested objects (e.g., MedicalHistoryDTO) once InfectionEntity is persisted.
}
