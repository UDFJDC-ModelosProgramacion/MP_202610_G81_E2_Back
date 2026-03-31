package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for AdoptionEntity.
 * Uses IDs for relationships to avoid circular serialization.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdoptionDTO {
    private Long id;
    private LocalDate officialDate;
    private Boolean contractSigned;
    private String status;
    private Long adopterId;
    private String adopterName;
    private Long petId;
    private String petName;
    private Long shelterId;
    private String shelterName;
    private Long trialCohabitationId;
}
