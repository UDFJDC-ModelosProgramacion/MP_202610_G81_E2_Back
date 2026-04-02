package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.VaccineEntryEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VaccineDTO {
    private Long id;
    private String vaccineName;
    private LocalDate adminDate;
    private LocalDate nextDueDate;
    private Long medicalHistoryId;

}
