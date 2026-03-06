package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import Java.time.localDateTime;
import java.util.Date;

@Data
@BaseEntity
public class TrialCohabitation extends AdoptionProcess{
    private LocalDate startDate;
    private LocalDate endDate;
    private String periodicNotes;
    private Boolean isSuccessfull;

    @OneToOne
    @JoinColumn(name = "adoption_request_id")
    @PodamExclude
    private AdoptionRequestEntity adoptionRequest;

    @OneToOne
    @JoinColumn(name = "Adoption")
    @PodamExclude
    private Adoption finalAdoption;

    


}