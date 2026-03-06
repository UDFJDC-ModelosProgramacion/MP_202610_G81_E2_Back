package co.edu.udistrital.mdp.pets.entities;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class VeterinarianEntity extends UserEntity {

    private String licenseNumber;
    private String availabilitySchedule;
    private String name;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private ShelterEntity shelter;

    @ManyToMany
    @JoinTable(
        name = "vet_speciality_relation",
        joinColumns = @JoinColumn(name = "vet_id"),
        inverseJoinColumns = @JoinColumn(name = "speciality_id")
    )
    private List<VetSpecialityEntity> specialities;
}
