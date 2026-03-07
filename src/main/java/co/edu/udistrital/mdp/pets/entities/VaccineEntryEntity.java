package co.edu.udistrital.mdp.pets.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class VaccineEntryEntity extends BaseEntity {
          private String vaccineName;
          private Date adminDate;
          private Date nextDueDate;
          
          @ManyToOne
          @JoinColumn(name = "medical_history_id")
          private MedicalHistoryEntity medicalHistory;
}
