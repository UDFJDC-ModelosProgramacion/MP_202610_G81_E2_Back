package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class PetEntity extends BaseEntity {
    private String name;
    private String species;
    private String breed;
    private LocalDate bornDate;
    private String sex;
    private String size;
    private String temperament;
    private String specificNeeds;
    private String originLocation;
    private Boolean isRescued;
    private String status;
    private String photo;

    @OneToOne(mappedBy = "pet", cascade = CascadeType.ALL)
    private MedicalHistoryEntity medicalHistory;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @PodamExclude
    private ShelterEntity shelter;

    @OneToMany(mappedBy = "pet")
    private List<AdoptionEntity> adoptionRequests = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public LocalDate getBornDate() {
        return bornDate;
    }

    public void setBornDate(LocalDate bornDate) {
        this.bornDate = bornDate;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getTemperament() {
        return temperament;
    }

    public void setTemperament(String temperament) {
        this.temperament = temperament;
    }

    public String getSpecificNeeds() {
        return specificNeeds;
    }

    public void setSpecificNeeds(String specificNeeds) {
        this.specificNeeds = specificNeeds;
    }

    public String getOriginLocation() {
        return originLocation;
    }

    public void setOriginLocation(String originLocation) {
        this.originLocation = originLocation;
    }

    public Boolean getIsRescued() {
        return isRescued;
    }

    public void setIsRescued(Boolean isRescued) {
        this.isRescued = isRescued;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public ShelterEntity getShelter() {
        return shelter;
    }

    public void setShelter(ShelterEntity shelter) {
        this.shelter = shelter;
    }
}