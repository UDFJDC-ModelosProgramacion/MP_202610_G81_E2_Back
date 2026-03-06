package co.edu.udistrital.mdp.pets.entities;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class TrialCohabitationEntity extends BaseEntity {
    private Date startDate;
    private Date endDate;
    private String observations;

    @OneToOne
    @JoinColumn(name = "adoption_request_id")
    @PodamExclude
    private AdoptionRequestEntity adoptionRequest;

    @OneToOne
    @JoinColumn(name = "Adoption")
    @PodamExclude
    private AdoptionEntity finalAdoption;

    @OneToOne(mappedBy = "trialcohabitation")
    private AdoptionEntity adoption;
}
