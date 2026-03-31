package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MedicalEventDTO {
    private String eventType;
    private String description;
    private LocalDate eventDate;

}
