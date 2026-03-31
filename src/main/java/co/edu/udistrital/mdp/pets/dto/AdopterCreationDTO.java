package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class AdopterCreationDTO {
          private String status;
          private String address;
          private String city;
}
