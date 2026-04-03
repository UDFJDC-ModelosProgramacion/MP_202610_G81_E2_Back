package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ShelterDTO {
    private Long id;
    private String shelterName;
    private String nit;
    private String phoneNumber;
    private String address;
    private String status;
    private String city;
    private String locationDetails;
    private String description;
    private String websiteUrl;

    public ShelterDTO(ShelterEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.shelterName = entity.getShelterName();
            this.nit = entity.getNit();
            this.phoneNumber = entity.getPhoneNumber();
            this.address = entity.getAddress();
            this.status = entity.getStatus();
            this.city = entity.getCity();
            this.locationDetails = entity.getLocationDetails();
            this.description = entity.getDescription();
            this.websiteUrl = entity.getWebsiteUrl();
        }
    }

    public ShelterEntity toEntity() {
        ShelterEntity entity = new ShelterEntity();
        entity.setId(this.id);
        entity.setShelterName(this.shelterName);
        entity.setNit(this.nit);
        entity.setPhoneNumber(this.phoneNumber);
        entity.setAddress(this.address);
        entity.setStatus(this.status);
        entity.setCity(this.city);
        entity.setLocationDetails(this.locationDetails);
        entity.setDescription(this.description);
        entity.setWebsiteUrl(this.websiteUrl);
        return entity;
    }
}
