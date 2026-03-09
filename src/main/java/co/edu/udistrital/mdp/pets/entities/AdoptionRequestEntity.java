package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class AdoptionRequestEntity extends BaseEntity {

    private LocalDate requestDate;

    private String status;

    private String comments;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    @PodamExclude
    private AdopterEntity adopter;

    @OneToOne(mappedBy = "adoptionRequest", cascade = CascadeType.ALL)
    @PodamExclude
    private AdoptionProcessEntity adoptionProcess;
}
