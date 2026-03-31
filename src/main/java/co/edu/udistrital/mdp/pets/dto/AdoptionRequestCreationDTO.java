package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AdoptionRequestCreationDTO {
          private Long id;
          private LocalDate requestDate;
          private String status;
          private String comments;
          private Long adopterId;
          private Long petId;
}
