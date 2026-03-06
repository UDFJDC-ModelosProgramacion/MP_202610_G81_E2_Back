package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class AdopterEntity extends UserEntity {

    private String address;
    private String city;
    private String housingType;
}