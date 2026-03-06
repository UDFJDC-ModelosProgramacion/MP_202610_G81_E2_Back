package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class AdoptionProcessEntity extends BaseEntity {
    private String status;
    private String description;
}
