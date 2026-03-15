package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class PetEntity extends BaseEntity {
    private String name;
    private String breed;
    private LocalDate bornDate;
    private String sex;
    private String size;
    private String temperament;
    private String specificNeeds;
    private boolean isRescued;
    private String status;

    @OneToOne(mappedBy = "pet", cascade = CascadeType.ALL)
    private MedicalHistoryEntity medicalHistory;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @PodamExclude
    private ShelterEntity shelter;

    @OneToMany(mappedBy = "pet")
    private List<AdoptionEntity> adoptionRequests = new ArrayList<>();
}