package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Importación clave
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

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
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class, 
  property = "id")
public class PetEntity extends BaseEntity {
    private String name;
    private String breed;
    private LocalDate bornDate;
    private String sex;
    private String size;
    private String temperament;
    private String specificNeeds;
    private Boolean isRescued;
    private String status;

    @OneToOne(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("pet") // Evita ciclo con la historia médica
    private MedicalHistoryEntity medicalHistory;

    @ManyToOne
    @JoinColumn(name = "shelter_id")
    @PodamExclude
    @JsonIgnoreProperties("pets") // Evita que el shelter intente listar todas sus mascotas de nuevo
    private ShelterEntity shelter;

    @OneToMany(mappedBy = "pet")
    @PodamExclude
    @JsonIgnoreProperties("pet") // Evita que la solicitud de adopción llame a la mascota en bucle
    private List<AdoptionEntity> adoptionRequests = new ArrayList<>();

    @OneToMany(mappedBy = "pet")
    @JsonIgnore // <--- ESTO CORTA EL LOOP. No intentará listar las adopciones de la mascota.
    private List<AdoptionEntity> adoptions = new ArrayList<>();
}