package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import java.time.LocalDate;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AdoptionProcessEntity extends BaseEntity {
    private String status;
    private LocalDate creationDate;
    private String description;

    @OneToOne
    @JoinColumn(name = "adoption_request_id")
    private AdoptionRequestEntity adoptionRequest;

    @OneToOne(mappedBy = "adoptionProcess", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @PodamExclude
    private ReturnCaseEntity returnCase;
}