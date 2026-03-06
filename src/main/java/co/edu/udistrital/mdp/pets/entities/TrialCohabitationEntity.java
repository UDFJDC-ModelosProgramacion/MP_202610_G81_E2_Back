package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class TrialCohabitationEntity extends AdoptionProcessEntity {
    private LocalDate startDate;
    private LocalDate endDate;
    private String periodicNotes;
    private Boolean isSuccessfull;

    @OneToOne
    @JoinColumn(name = "adoption_request_id")
    @PodamExclude
    private AdoptionRequestEntity adoptionRequest;

    @OneToOne(mappedBy = "trialCohabitation")
    @PodamExclude
    private AdoptionEntity adoption;
}
