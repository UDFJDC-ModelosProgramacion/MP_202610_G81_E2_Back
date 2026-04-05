package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdoptionProcessDTO {
    private Long id;
    private LocalDate creationDate;
    private String status;
    private Long adoptionRequestId;
    private Long petId;

    public AdoptionProcessDTO(AdoptionProcessEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.creationDate = entity.getCreationDate();
            this.status = entity.getStatus();
            if (entity.getAdoptionRequest() != null) this.adoptionRequestId = entity.getAdoptionRequest().getId();
            if (entity.getPet() != null) this.petId = entity.getPet().getId();
        }
    }

    public AdoptionProcessEntity toEntity() {
        TrialCohabitationEntity entity = new TrialCohabitationEntity();
        entity.setId(this.id);
        entity.setCreationDate(this.creationDate);
        entity.setStatus(this.status);
        if (this.adoptionRequestId != null) {
            AdoptionRequestEntity adoptionRequest = new AdoptionRequestEntity();
            adoptionRequest.setId(this.adoptionRequestId);
            entity.setAdoptionRequest(adoptionRequest);
        }
        if (this.petId != null) {
            PetEntity pet = new PetEntity();
            pet.setId(this.petId);
            entity.setPet(pet);
        }
        return entity;
    }
}
