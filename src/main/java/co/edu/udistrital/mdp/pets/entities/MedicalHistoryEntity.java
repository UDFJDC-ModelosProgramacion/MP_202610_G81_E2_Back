package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class MedicalHistoryEntity extends BaseEntity {
    private LocalDate createdDate;
    private LocalDate lastUpdated;

    @OneToOne
    @JoinColumn(name = "pet_id")
    @JsonIgnore
    @PodamExclude
    private PetEntity pet;

    @OneToMany(mappedBy = "medicalHistory")
    @PodamExclude
    private List<MedicalEventEntity> medicalEvents = new ArrayList<>();

    @OneToMany(mappedBy = "medicalHistory")
    @PodamExclude
    private List<VaccineEntryEntity> vaccineEntries = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "veterinarian_id")
    @PodamExclude
    private VeterinarianEntity veterinarian;

    @OneToMany(mappedBy = "medicalHistory")
    @PodamExclude
    private List<MedicationEntity> medications = new ArrayList<>();

    @OneToMany(mappedBy = "medicalHistory")
    @PodamExclude
    private List<InfectionEntity> infections = new ArrayList<>();

    @OneToMany(mappedBy = "medicalHistory")
    @PodamExclude
    private List<ProcedureEntity> procedures = new ArrayList<>();
}
