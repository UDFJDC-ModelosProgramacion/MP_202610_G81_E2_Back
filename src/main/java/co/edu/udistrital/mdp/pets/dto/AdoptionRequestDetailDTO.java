package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdoptionRequestDetailDTO extends AdoptionRequestDTO {
    private AdopterDTO adopter;
    private PetDTO1 pet;
    private Long adoptionProcessId;

    public AdoptionRequestDetailDTO(AdoptionRequestEntity entity) {
        super(entity);
        if (entity != null) {
            if (entity.getAdopter() != null) {
                this.adopter = new AdopterDTO(entity.getAdopter());
            }
            if (entity.getPet() != null) {
                this.pet = new PetDTO1(entity.getPet());
            }
            if (entity.getAdoptionProcess() != null) {
                this.adoptionProcessId = entity.getAdoptionProcess().getId();
            }
        }
    }
}
