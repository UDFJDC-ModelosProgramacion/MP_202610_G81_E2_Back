package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ShelterDetailDTO extends ShelterDTO {
    private List<ShelterMediaDTO> mediaItems = new ArrayList<>();
    private List<ReviewDTO> reviews = new ArrayList<>();
    private List<Long> veterinarianIds = new ArrayList<>();

    public ShelterDetailDTO(ShelterEntity entity) {
        super(entity);
        if (entity != null) {
            if (entity.getMediaItems() != null) {
                entity.getMediaItems().forEach(m -> mediaItems.add(new ShelterMediaDTO(m)));
            }
            if (entity.getReviews() != null) {
                entity.getReviews().forEach(r -> reviews.add(new ReviewDTO(r)));
            }
            if (entity.getVeterinarians() != null) {
                entity.getVeterinarians().forEach(v -> veterinarianIds.add(v.getId()));
            }
        }
    }
}
