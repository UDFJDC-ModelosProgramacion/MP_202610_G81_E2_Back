package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ShelterCreationDTO {
          private Long id;
          private String shelterName;
          private String nit;
          private String phoneNumber;
          private String address;
          private String status;
          private String city;
          private String locationDetails;
          private String description;
          private String websiteUrl;
}
