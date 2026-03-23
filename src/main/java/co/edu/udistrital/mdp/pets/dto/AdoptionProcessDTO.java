package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for AdoptionProcessEntity.
 * Breaks bidirectional cycles by using IDs instead of full objects.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionProcessDTO {
    private Long id;
    private LocalDate creationDate;
    private String status;
    
    // Use IDs instead of full objects to break cycles
    private Long adoptionRequestId;
    private Long returnCaseId; // Only ID, not full object
    private Long petId;
    private String petName;
    private Integer reviewsCount = 0;
}
