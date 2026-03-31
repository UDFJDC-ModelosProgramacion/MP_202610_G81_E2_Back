package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PetCreationDTO {
          private String name;
          private String breed;
          private LocalDate bornDate;
          private String sex;
          private String size;
          private String temperament;
          private String specificNeeds;
          private Boolean isRescued;
          private String status;

          private Long shelterId;
}