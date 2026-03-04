package co.edu.udistrital.mdp.pets.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class MedicalHistoryEntity extends BaseEntity {
          private Date createdDate;
          private Date lastUpdated;
}
