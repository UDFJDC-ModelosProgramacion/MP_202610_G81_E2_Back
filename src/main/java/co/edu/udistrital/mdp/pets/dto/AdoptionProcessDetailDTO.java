package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdoptionProcessDetailDTO extends AdoptionProcessDTO {
    private ReturnCaseDTO returnCase;
    private List<ReviewDTO> reviews = new ArrayList<>();

    public AdoptionProcessDetailDTO(AdoptionProcessEntity entity) {
        super(entity);
        if (entity != null) {
            if (entity.getReturnCase() != null) this.returnCase = new ReturnCaseDTO(entity.getReturnCase());
            if (entity.getReviews() != null) {
                entity.getReviews().forEach(r -> reviews.add(new ReviewDTO(r)));
            }
        }
    }
}
