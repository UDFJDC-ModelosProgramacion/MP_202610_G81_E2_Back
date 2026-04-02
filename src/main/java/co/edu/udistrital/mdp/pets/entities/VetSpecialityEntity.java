package co.edu.udistrital.mdp.pets.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<VeterinarianEntity> getVeterinarians() {
        return veterinarians;
    }

    public void setVeterinarians(List<VeterinarianEntity> veterinarians) {
        this.veterinarians = veterinarians;
    }
}
