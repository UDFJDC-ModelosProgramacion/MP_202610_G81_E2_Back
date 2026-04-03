package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdoptionRequestDTO {
    private Long id;
    private LocalDate requestDate;
    private String status;
    private String comments;
    private Long adopterId;
    private Long petId;

    public AdoptionRequestDTO(AdoptionRequestEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.requestDate = entity.getRequestDate();
            this.status = entity.getStatus();
            this.comments = entity.getComments();
            if (entity.getAdopter() != null) {
                this.adopterId = entity.getAdopter().getId();
            }
            if (entity.getPet() != null) {
                this.petId = entity.getPet().getId();
            }
        }
    }

    public AdoptionRequestEntity toEntity() {
        AdoptionRequestEntity entity = new AdoptionRequestEntity();
        entity.setId(this.id);
        entity.setRequestDate(this.requestDate);
        entity.setStatus(this.status);
        entity.setComments(this.comments);
        return entity;
    }
}
