package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(fetch = FetchType.LAZY)
    private List<VetSpecialityEntity> specialties = new ArrayList<>();
}
