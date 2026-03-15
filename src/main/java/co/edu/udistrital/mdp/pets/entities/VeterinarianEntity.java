package co.edu.udistrital.mdp.pets.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class VeterinarianEntity extends UserEntity {

    private String licenseNumber;
    private String availabilitySchedule;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @PodamExclude
    private ShelterEntity shelter;

    @PodamExclude
    @ManyToMany
    @JoinTable(name = "vet_speciality_relation", joinColumns = @JoinColumn(name = "vet_id"), inverseJoinColumns = @JoinColumn(name = "speciality_id"))
    private List<VetSpecialityEntity> specialities = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @PodamExclude
    @OneToMany(mappedBy = "veterinarian", fetch = FetchType.LAZY)
    private List<MedicalHistoryEntity> medicalHistories = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @PodamExclude
    @OneToMany(mappedBy = "veterinarian", fetch = FetchType.LAZY)
    private List<MedicalEventEntity> medicalEvents = new ArrayList<>();
}
