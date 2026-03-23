package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for VeterinarianEntity.
 * Extends UserDTO and adds veterinarian-specific fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VeterinarianDTO extends UserDTO {
    private String licenseNumber;
    private String availabilitySchedule;
    
    // References as IDs to prevent cycles
    private Long shelterId;
    private String shelterName;
    private Integer specialitiesCount = 0;
    private Integer medicalHistoriesCount = 0;
    private Integer medicalEventsCount = 0;
}
