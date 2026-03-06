package co.edu.udistrital.mdp.pets.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class AdoptionEntity extends BaseEntity {

    private Date officialDate;
    private Boolean contractSigned;

    @OneToOne
    @JoinColumn(name = "trial_id")
    @PodamExclude
    private TrialCohabitationEntity trialCohabitation;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    @PodamExclude
    private PetEntity pet;
}
