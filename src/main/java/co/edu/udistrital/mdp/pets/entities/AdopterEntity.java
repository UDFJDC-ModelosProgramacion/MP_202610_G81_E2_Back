package co.edu.udistrital.mdp.pets.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class AdopterEntity extends UserEntity {

    private String address;
    private String city;
    private String housingType;
    private String status;

    @OneToMany(mappedBy = "adopter")
    @PodamExclude
    private List<AdoptionRequestEntity> adoptionRequests = new ArrayList<>();

    @OneToMany(mappedBy = "adopter")
    private List<ReviewEntity> reviews = new ArrayList<>();
}
