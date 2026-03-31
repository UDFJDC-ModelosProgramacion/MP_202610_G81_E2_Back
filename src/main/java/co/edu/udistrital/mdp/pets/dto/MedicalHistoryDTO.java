package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MedicalHistoryDTO {
    private Long id;
    private LocalDate createdDate;
    private LocalDate lastUpdated;
    private Long petId;
    private Long veterinarianId;

}
