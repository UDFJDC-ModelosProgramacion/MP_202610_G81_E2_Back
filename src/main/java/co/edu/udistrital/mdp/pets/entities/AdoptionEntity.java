package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

import uk.co.jemos.podam.common.PodamExclude;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class AdoptionEntity extends BaseEntity {

    private LocalDate officialDate;
    private Boolean contractSigned;

    @OneToOne
    @JoinColumn(name = "trial_cohabitation_id")
    @PodamExclude
    private TrialCohabitationEntity trialCohabitation;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    @PodamExclude
    private PetEntity pet;
}
