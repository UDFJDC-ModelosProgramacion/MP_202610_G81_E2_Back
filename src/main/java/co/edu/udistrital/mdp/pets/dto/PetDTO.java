package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for PetEntity.
 * Prevents circular reference by using shelter/medical history IDs instead of full objects.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDTO {
    private Long id;
    private String name;
    private String breed;
    private LocalDate bornDate;
    private String sex;
    private String size;
    private String temperament;
    private String specificNeeds;
    private Boolean isRescued;
    private String status;
    
    // Use IDs instead of full objects to break cycles
    private Long shelterId;
    private String shelterName;
    private Long medicalHistoryId;
    private Integer adoptionRequestsCount = 0;
}
