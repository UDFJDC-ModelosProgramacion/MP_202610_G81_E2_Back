package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private Integer rating;
    private String comment;
    private Boolean isSuccessStory;
    private Long adopterId;
    private Long adoptionProcessId;
    private Long shelterId;

    public ReviewDTO(ReviewEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.rating = entity.getRating();
            this.comment = entity.getComment();
            this.isSuccessStory = entity.getIsSuccessStory();
            if (entity.getAdopter() != null) this.adopterId = entity.getAdopter().getId();
            if (entity.getAdoptionProcess() != null) this.adoptionProcessId = entity.getAdoptionProcess().getId();
            if (entity.getShelter() != null) this.shelterId = entity.getShelter().getId();
        }
    }

    public ReviewEntity toEntity() {
        ReviewEntity entity = new ReviewEntity();
        entity.setId(this.id);
        entity.setRating(this.rating);
        entity.setComment(this.comment);
        entity.setIsSuccessStory(this.isSuccessStory);
        return entity;
    }
}
