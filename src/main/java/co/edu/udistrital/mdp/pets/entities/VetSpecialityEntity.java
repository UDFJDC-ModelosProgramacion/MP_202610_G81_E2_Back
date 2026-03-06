package co.edu.udistrital.mdp.pets.entities;

import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class VetSpecialityEntity extends BaseEntity {

    private String name;
    private String description;

    @ManyToMany(mappedBy = "specialities")
    private List<VeterinarianEntity> veterinarians;
}
