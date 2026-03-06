package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import java.time.LocalDateTime;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AdoptionProcessEntity extends BaseEntity {

    private LocalDate creationDate;

    private String status;

    @OneToOne(mappedBy = "adoptionProcessEntity", cascade = CascadeType.ALL)
    @PodamExclude
    private ReturnCaseEntity returnCase;
}