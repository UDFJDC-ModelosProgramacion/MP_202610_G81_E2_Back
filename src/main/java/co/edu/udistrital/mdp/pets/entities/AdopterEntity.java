package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class AdopterEntity extends UserEntity {

    private String address;
    private String city;
    private String housingType;

    @OneToMany(mappedBy = "adopter")
    private List<AdoptionRequestEntity> adoptionRequests;

    @OneToMany(mappedBy = "adopter")
    private List<ReviewEntity> reviews;
}