package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.ArrayList;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class AdopterEntity extends UserEntity {

    private String address;
    private String city;
    private String housingType;

    @OneToMany(mappedBy = "adopter")
    @PodamExclude
    private List<AdoptionRequestEntity> adoptionRequests = new ArrayList<>();

    @OneToMany(mappedBy = "adopter")
    private List<ReviewEntity> reviews = new ArrayList<>();
}
