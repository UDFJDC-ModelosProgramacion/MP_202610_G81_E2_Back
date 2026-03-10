package co.edu.udistrital.mdp.pets.entities;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class VetSpecialityEntity extends BaseEntity {

    private String name;
    private String description;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToMany(mappedBy = "specialities")
    private List<VeterinarianEntity> veterinarians = new ArrayList<>();
}
