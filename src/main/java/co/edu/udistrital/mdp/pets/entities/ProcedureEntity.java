package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ProcedureEntity extends BaseEntity {
    private String procedureName;
    private String description;
    private LocalDate procedureDate;
    private String outcome;

    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    @PodamExclude
    private MedicalHistoryEntity medicalHistory;

    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    @PodamExclude
    private VeterinarianEntity veterinarian;
}
