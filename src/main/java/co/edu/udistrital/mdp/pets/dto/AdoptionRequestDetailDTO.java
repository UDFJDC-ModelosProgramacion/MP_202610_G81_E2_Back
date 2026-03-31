package co.edu.udistrital.mdp.pets.dto;


import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdoptionRequestDetailDTO extends AdoptionRequestDTO {
    private AdopterDTO adopter;

    private Long adoptionProcessId;

}
