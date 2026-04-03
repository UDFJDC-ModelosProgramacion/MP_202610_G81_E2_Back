package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Importación necesaria
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import uk.co.jemos.podam.common.PodamExclude;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class, 
  property = "id")
public class AdoptionEntity extends BaseEntity {

    private LocalDate officialDate;
    private Boolean contractSigned;

    @Enumerated(EnumType.STRING)
    private AdoptionStatus status;

    @ManyToOne
    @JoinColumn(name = "adopter_id")
    // Evita que el adoptante intente cargar sus propias adopciones en bucle
    @JsonIgnoreProperties("adoptions")
    @PodamExclude
    private AdopterEntity adopter;

    @ManyToOne
    @JoinColumn(name = "pet_id")
    // Evita que la mascota intente cargar sus procesos de adopción en bucle
    @JsonIgnoreProperties("adoptions")
    @PodamExclude
    private PetEntity pet;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @PodamExclude
    // Si ShelterEntity tiene una lista de mascotas o adopciones, esto evita el ciclo
    @JsonIgnoreProperties({"pets", "adoptions"})
    private ShelterEntity shelter;

    @OneToOne
    @JoinColumn(name = "trial_cohabitation_id")
    @PodamExclude
    // Evita ciclos con la entidad de convivencia de prueba
    @JsonIgnoreProperties("adoption")
    private TrialCohabitationEntity trialCohabitation;
}