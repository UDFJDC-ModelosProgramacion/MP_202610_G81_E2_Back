package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReviewDetailDTO extends ReviewDTO {
    public ReviewDetailDTO(ReviewEntity entity) {
        super(entity);
    }
}
