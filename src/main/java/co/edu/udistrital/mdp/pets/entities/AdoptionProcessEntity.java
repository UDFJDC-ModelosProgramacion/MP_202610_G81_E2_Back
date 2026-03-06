package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class AdoptionProcessEntity extends BaseEntity {
    private String status;
    private String description;

    @OneToOne(mappedBy = "adoptionProcess", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PodamExclude
    private ReturnCaseEntity returnCase;
}