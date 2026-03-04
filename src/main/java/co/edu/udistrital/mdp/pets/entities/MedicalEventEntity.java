package co.edu.udistrital.mdp.pets.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class MedicalEventEntity extends BaseEntity{
          private String eventType;
          private String description;
          private Date eventDate;
}
