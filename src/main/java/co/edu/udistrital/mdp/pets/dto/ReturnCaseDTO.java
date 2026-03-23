package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for ReturnCaseEntity.
 * Uses only ID for adoptionProcess to break the bidirectional cycle.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReturnCaseDTO {
    private Long id;
    private LocalDate returnDate;
    private String reason;
    private String details;
    
    // Use ID instead of full object to break cycles
    private Long adoptionProcessId;
}
