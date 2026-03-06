package co.edu.udistrital.mdp.pets.entities;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class AdoptionRequestEntity extends BaseEntity {
    private Date requestDate;
    private String status;
    private String message;

     @ManyToOne
     @JoinColumn(name = "adopter_id" )
     @PodamExclude
     private AdopterEntity adopter;

     @OneToOne(mappedBy = "adoptionRequest", cascade = CascadeType.ALL)
     @PodamExclude
     private TrialCohabitationEntity trialPeriod;

     @OneToOne(mappedBy = "adoptionRequest")
     private AdoptionProcessEntity adoptionProcess;

     @ManyToOne
     @JoinColumn(name = "pet_id")
     @PodamExclude
     private PetEntity pet;
}
