package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Detail DTO for Medications.
 * Extends MedicationDTO with additional nested information when available.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MedicationDetailDTO extends MedicationDTO {
    // Extend with nested objects (e.g., MedicalHistoryDTO) once MedicationEntity is created.
}
