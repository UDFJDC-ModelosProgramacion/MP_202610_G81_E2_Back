package co.edu.udistrital.mdp.pets.entities;
import jakarta.persistence.*;
import lombok.Data;
import java.time.Date;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class ReturnCaseEntity extends BaseEntity{
    private Date returnDate;
    private String reason;
    private String details;

    //@OneToOne
    //@JoinColumn(name = "adoption_id")
    //@PodamExclude
    //private AdoptionEntity adoption;
}
