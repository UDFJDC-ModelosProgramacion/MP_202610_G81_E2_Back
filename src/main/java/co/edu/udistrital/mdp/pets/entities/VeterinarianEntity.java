package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private ShelterEntity shelter;

    @OneToMany(fetch = FetchType.LAZY)
    private List<VetSpecialityEntity> specialties = new ArrayList<>();
}
