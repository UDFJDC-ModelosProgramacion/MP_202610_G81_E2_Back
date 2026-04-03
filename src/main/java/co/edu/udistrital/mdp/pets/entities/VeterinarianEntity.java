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

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getAvailabilitySchedule() {
        return availabilitySchedule;
    }

    public void setAvailabilitySchedule(String availabilitySchedule) {
        this.availabilitySchedule = availabilitySchedule;
    }

    public ShelterEntity getShelter() {
        return shelter;
    }

    public void setShelter(ShelterEntity shelter) {
        this.shelter = shelter;
    }

    public List<VetSpecialityEntity> getSpecialities() {
        return specialities;
    }

    public void setSpecialities(List<VetSpecialityEntity> specialities) {
        this.specialities = specialities;
    }

    public List<MedicalHistoryEntity> getMedicalHistories() {
        return medicalHistories;
    }

    public void setMedicalHistories(List<MedicalHistoryEntity> medicalHistories) {
        this.medicalHistories = medicalHistories;
    }

    public List<MedicalEventEntity> getMedicalEvents() {
        return medicalEvents;
    }

    public void setMedicalEvents(List<MedicalEventEntity> medicalEvents) {
        this.medicalEvents = medicalEvents;
    }
}
