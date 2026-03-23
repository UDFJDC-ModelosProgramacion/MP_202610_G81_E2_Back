package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for AdoptionRequestEntity.
 * Breaks bidirectional cycle by using only ID for adoptionProcess.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionRequestDTO {
    private Long id;
    private LocalDate requestDate;
    private String status;
    private String comments;
    
    // Use IDs instead of full objects to break cycles
    private Long adopterId;
    private String adopterName;
    private Long petId;
    private String petName;
    private Long adoptionProcessId; // Only ID, not full object
}
