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
public class InfectionEntity extends BaseEntity {
    private String infectionType;
    private String description;
    private String treatment;
    private LocalDate diagnosisDate;
    private LocalDate recoveryDate;

    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    @PodamExclude
    private MedicalHistoryEntity medicalHistory;
}
