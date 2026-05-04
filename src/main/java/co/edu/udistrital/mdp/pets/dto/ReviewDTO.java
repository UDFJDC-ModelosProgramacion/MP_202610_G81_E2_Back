package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
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
        if (this.adopterId != null) {
            AdopterEntity adopter = new AdopterEntity();
            adopter.setId(this.adopterId);
            entity.setAdopter(adopter);
        }
        if (this.adoptionProcessId != null) {
            co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity process = new co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity();
            process.setId(this.adoptionProcessId);
            entity.setAdoptionProcess(process);
        }
        if (this.shelterId != null) {
            ShelterEntity shelter = new ShelterEntity();
            shelter.setId(this.shelterId);
            entity.setShelter(shelter);
        }
        return entity;
    }
}
