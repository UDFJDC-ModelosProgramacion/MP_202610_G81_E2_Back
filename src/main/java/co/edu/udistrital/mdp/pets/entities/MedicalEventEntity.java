package co.edu.udistrital.mdp.pets.entities;

import java.sql.Date;

import jakarta.persistence.*;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class MedicalEventEntity extends BaseEntity{
          private String eventType;
          private String description;
          private Date eventDate;

          @ManyToOne
          @JoinColumn(name = "medical_history_id")
          @PodamExclude
          private MedicalHistoryEntity medicalHistory;
}
