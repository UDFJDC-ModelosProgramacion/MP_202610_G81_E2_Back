package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.Date;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class AdoptionEntity extends BaseEntity {

    private Date officialDate;
    private Boolean contractSigned;

    @OneToOne
    @JoinColumn(name = "adoption_request_id")
    @PodamExclude
    private AdoptionRequestEntity adoptionRequest;

    //@OneToOne(mappedBy = "adoption")
    //@PodamExclude
    //private ReturnCaseEntity returnCase;
}
