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

    @Enumerated(EnumType.STRING)
    private AdoptionStatus status;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    @PodamExclude
    private AdopterEntity adopter;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @PodamExclude
    private ShelterEntity shelter;

    @OneToOne
    @JoinColumn(name = "trial_cohabitation_id")
    @PodamExclude
    private TrialCohabitationEntity trialCohabitation;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    @PodamExclude
    private PetEntity pet;
}