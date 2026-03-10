package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class MedicalEventEntity extends BaseEntity {
    private String eventType;
    private String description;
    private LocalDate eventDate;

    @ManyToOne
    @JoinColumn(name = "medical_history_id")
    @PodamExclude
    private MedicalHistoryEntity medicalHistory;
}
