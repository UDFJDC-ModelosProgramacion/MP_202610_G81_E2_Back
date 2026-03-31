package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;
import java.util.ArrayList;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class MedicalHistoryCreationDTO {
          private Long id;
          private LocalDate createdDate;
          private LocalDate lastUpdated;
          private Long petId;
          private Long veterinarianId; 
          private ArrayList<MedicalEventDTO> events;
          private ArrayList<VaccineDTO> vaccine;
}
