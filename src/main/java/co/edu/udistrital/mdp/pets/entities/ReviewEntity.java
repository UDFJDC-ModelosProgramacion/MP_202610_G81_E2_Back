package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity

public class ReviewEntity extends BaseEntity{
    private Integer rating;
    private String comment;
    private Boolean isSuccessStory;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    @PodamExclude
    private AdopterEntity<?> adopter;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private ShelterEntity shelter;
}
