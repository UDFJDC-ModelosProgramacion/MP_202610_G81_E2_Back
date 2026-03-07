package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Data
@Entity
public class TrialCohabitationEntity extends AdoptionProcessEntity {

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean approved;

}
