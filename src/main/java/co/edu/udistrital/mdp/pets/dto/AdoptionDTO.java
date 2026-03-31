package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdoptionDTO {
    private Long id;
    private LocalDate officialDate;
    private Boolean contractSigned;
    private String status;
    private Long adopterId;
    private Long petId;
    private Long shelterId;

}
