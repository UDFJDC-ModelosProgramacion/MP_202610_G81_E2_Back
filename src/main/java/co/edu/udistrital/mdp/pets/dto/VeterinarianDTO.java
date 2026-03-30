package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VeterinarianDTO extends UserDTO {
    private String licenseNumber;
    private String availabilitySchedule;
    private Long shelterId;

    public VeterinarianDTO(VeterinarianEntity entity) {
        super(entity);
        if (entity != null) {
            this.licenseNumber = entity.getLicenseNumber();
            this.availabilitySchedule = entity.getAvailabilitySchedule();
            if (entity.getShelter() != null) this.shelterId = entity.getShelter().getId();
        }
    }

    public VeterinarianEntity toEntity() {
        VeterinarianEntity entity = new VeterinarianEntity();
        entity.setId(this.getId());
        this.copyToEntity(entity);
        entity.setLicenseNumber(this.licenseNumber);
        entity.setAvailabilitySchedule(this.availabilitySchedule);
        return entity;
    }
}
