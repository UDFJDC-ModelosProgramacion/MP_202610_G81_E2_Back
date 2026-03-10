package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ReturnCaseEntity extends BaseEntity {

    private LocalDate returnDate;

    private String reason;

    private String details;

    @OneToOne
    @JoinColumn(name = "adoption_process_id")
    @PodamExclude
    private AdoptionProcessEntity adoptionProcess;
}