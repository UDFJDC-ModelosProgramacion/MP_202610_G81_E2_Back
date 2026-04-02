package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.ShelterMediaEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShelterMediaDTO {
    private Long id;
    private String mediaUrl;
    private String mediaType;
    private String description;
    private Long shelterId;

    public ShelterMediaDTO(ShelterMediaEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.mediaUrl = entity.getMediaUrl();
            this.mediaType = entity.getMediaType();
            this.description = entity.getDescription();
            if (entity.getShelter() != null) this.shelterId = entity.getShelter().getId();
        }
    }

    public ShelterMediaEntity toEntity() {
        ShelterMediaEntity entity = new ShelterMediaEntity();
        entity.setId(this.id);
        entity.setMediaUrl(this.mediaUrl);
        entity.setMediaType(this.mediaType);
        entity.setDescription(this.description);
        return entity;
    }
}
