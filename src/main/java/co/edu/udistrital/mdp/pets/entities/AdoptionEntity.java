package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class AdoptionEntity<TrialCohabitation> extends BaseEntity {

    private Date officialDate;
    private Boolean contractSigned;

    @OneToOne
    @JoinColumn(name = "trial_id")
    @PodamExclude
    private TrialCohabitation trialcohabitation;
}
