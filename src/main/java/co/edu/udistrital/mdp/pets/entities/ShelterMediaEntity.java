package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ShelterMediaEntity extends BaseEntity {

    private String mediaUrl;
    private String mediaType;
    private String description;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private ShelterEntity shelter;
}
