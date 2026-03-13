package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AdoptionProcessEntity extends BaseEntity {

    private LocalDate creationDate;

    private String status;

    @OneToOne
    @JoinColumn(name = "adoption_request_id")
    @PodamExclude
    private AdoptionRequestEntity adoptionRequest;

    @OneToOne(mappedBy = "adoptionProcess", cascade = CascadeType.ALL)
    @PodamExclude
    private ReturnCaseEntity returnCase;

    @OneToMany(mappedBy = "adoptionProcess")
    @PodamExclude
    private List<ReviewEntity> reviews = new ArrayList<>();
}