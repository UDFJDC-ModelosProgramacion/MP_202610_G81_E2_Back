package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
public class MedicalHistoryEntity extends BaseEntity {
    private LocalDate createdDate;
    private LocalDate lastUpdated;

    @OneToOne
    @JoinColumn(name = "pet_id")
    @PodamExclude
    private PetEntity pet;

    @OneToMany(mappedBy = "medicalHistory")
    @PodamExclude
    private List<MedicalEventEntity> medicalEvents = new ArrayList<>();

    @OneToMany(mappedBy = "medicalHistory")
    @PodamExclude
    private List<VaccineEntryEntity> vaccineEntries = new ArrayList<>();
}
