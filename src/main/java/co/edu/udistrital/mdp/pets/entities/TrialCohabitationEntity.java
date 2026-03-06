package co.edu.udistrital.mdp.pets.entities;

import java.util.Date;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class TrialCohabitationEntity extends BaseEntity {
    private Date startDate;
    private Date endDate;
    private String observations;
}
