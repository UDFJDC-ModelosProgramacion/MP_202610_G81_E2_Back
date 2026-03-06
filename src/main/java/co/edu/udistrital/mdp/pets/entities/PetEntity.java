package co.edu.udistrital.mdp.pets.entities;

import java.sql.Date;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class PetEntity extends BaseEntity {
          private String name;
          private String species;
          private String breed;
          private Date bornDate;
          private String sex;
          private String size;
          private String temperament;
          private String specificNeeds;
          private boolean isRescued;
          private String status;
}
