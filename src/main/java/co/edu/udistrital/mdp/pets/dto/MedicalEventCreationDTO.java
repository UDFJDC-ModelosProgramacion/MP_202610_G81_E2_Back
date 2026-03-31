package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MedicalEventCreationDTO {
          private Long id;
          private String eventType;
          private String description;
          private LocalDate eventDate;
          private Long medicalHistoryId;
          private Long veterinarianId;
}
