package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AdoptionCreationDTO {
          private Long id;
          private LocalDate officialDate;
          private Boolean contractSigned;
          private String status;
          private Long adopterId;
          private Long petId;
          private Long shelterId;

}
