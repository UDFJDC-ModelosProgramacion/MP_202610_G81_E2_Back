package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * DTO for AdopterEntity.
 * Extends UserDTO and adds adopter-specific fields.
 * Inherits cycle prevention from UserDTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdopterDTO extends UserDTO {
    private String address;
    private String city;
    private String housingType;
    private String status;
    
    // References as IDs/counts to prevent cycles
    private Integer adoptionRequestsCount = 0;
    private Integer reviewsCount = 0;
}
