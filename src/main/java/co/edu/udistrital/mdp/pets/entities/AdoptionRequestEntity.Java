package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import Java.time.localDateTime;
import java.util.Date;

@Data
@BaseEntity
public class AdoptionRequestEntity extends BaseEntity{
     private LocalDate requestDate;
     private String approvalStatus;

     @ManyToOne
     @JoinColumn(name = "adopter_id" )
     @PodamExclude
     private Adopter adopter;

     @OneToOne
     @JoinColumn(name = "TrialCohabitation")
     @PodamExclude
     private TrialCohabitation trialPeriod;

     @ManyToOne
     @JoinColumn(name = "pet_id")
     private Pet pet;

}