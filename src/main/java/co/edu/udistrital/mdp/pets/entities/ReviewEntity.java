package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ReviewEntity extends BaseEntity{
    private Integer rating;
    private String comment;
    private Boolean isSuccessStory;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    @PodamExclude
    private AdopterEntity adopter;

    @ManyToOne
    @JoinColumn(name = "adoption_process_id")
    @PodamExclude
    private AdoptionProcessEntity adoptionProcess;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @PodamExclude
    private ShelterEntity shelter;
}
