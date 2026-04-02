package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
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
        AdoptionProcessEntity entity = new co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity();
        entity.setId(this.id);
        entity.setCreationDate(this.creationDate);
        entity.setStatus(this.status);
        return entity;
    }
}
