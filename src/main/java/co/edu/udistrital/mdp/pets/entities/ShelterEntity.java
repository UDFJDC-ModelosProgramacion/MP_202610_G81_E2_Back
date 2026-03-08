package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.co.jemos.podam.common.PodamExclude;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ShelterEntity extends BaseEntity {

    private String shelterName;
    private String city;
    private String locationDetails;
    private String description;
    private String websiteUrl;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @PodamExclude
    @OneToMany(mappedBy = "shelter", fetch = FetchType.LAZY)
    private List<VeterinarianEntity> veterinarians = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @PodamExclude
    @OneToMany(mappedBy = "shelter", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ShelterMediaEntity> mediaItems = new ArrayList<>();

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @PodamExclude
    @OneToMany(mappedBy = "shelter")
    private List<ReviewEntity> reviews;
}
