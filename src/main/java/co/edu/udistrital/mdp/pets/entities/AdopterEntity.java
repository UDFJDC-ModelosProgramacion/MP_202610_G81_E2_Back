package co.edu.udistrital.mdp.pets.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // Cambiado para mayor compatibilidad
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class, 
  property = "id")
public class AdopterEntity extends UserEntity {

    private String address;
    private String city;
    private String housingType;
    private String status;

    @OneToMany(mappedBy = "adopter")
    @JsonIgnore // <--- ESTO CORTA EL LOOP. No serializará la lista de adopciones desde aquí.
    private List<AdoptionEntity> adoptions = new ArrayList<>();

    @OneToMany(mappedBy = "adopter")
    @PodamExclude
    // Hacemos lo mismo con las solicitudes para evitar recursión si estas apuntan de vuelta
    @JsonIgnoreProperties("adopter")
    private List<AdoptionRequestEntity> adoptionRequests = new ArrayList<>();

    @OneToMany(mappedBy = "adopter")
    @PodamExclude
    @JsonIgnoreProperties("adopter")
    private List<ReviewEntity> reviews = new ArrayList<>();
}