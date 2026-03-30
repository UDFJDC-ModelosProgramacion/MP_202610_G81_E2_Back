package co.edu.udistrital.mdp.pets.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class ShelterMediaEntity extends BaseEntity {

    private String mediaUrl;
    private String mediaType;
    private String description;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @PodamExclude
    @ManyToOne
    @JoinColumn(name = "shelter_id")
    private ShelterEntity shelter;

    public String getMediaUrl() {
        return mediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        this.mediaUrl = mediaUrl;
    }

    public String getMediaType() {
        return mediaType;
    }

    public void setMediaType(String mediaType) {
        this.mediaType = mediaType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ShelterEntity getShelter() {
        return shelter;
    }

    public void setShelter(ShelterEntity shelter) {
        this.shelter = shelter;
    }
}
