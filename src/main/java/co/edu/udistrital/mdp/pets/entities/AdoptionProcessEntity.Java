package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;
import Java.time.localDateTime;
import java.util.Date;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AdoptionProcess extends BaseEntity {

    private LocalDate creationDate;

    private String status;

}