package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class AdopterEntity<AdoptionRequest> extends UserEntity {

    private String address;
    private String city;
    private String housingType;

    @OneToMany(mappedBy = "adopter")
    private List<AdoptionRequest> adoptionRequest;

    @OneToMany(mappedBy = "adopter")
    private List<ReviewEntity> reviews;
}