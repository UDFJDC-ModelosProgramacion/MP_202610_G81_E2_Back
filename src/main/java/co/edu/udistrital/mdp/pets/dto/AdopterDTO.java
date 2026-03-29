package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdopterDTO extends UserDTO {
    private String address;
    private String city;
    private String housingType;
    private String status;

    public AdopterDTO(AdopterEntity entity) {
        super(entity);
        if (entity != null) {
            this.address = entity.getAddress();
            this.city = entity.getCity();
            this.housingType = entity.getHousingType();
            this.status = entity.getStatus();
        }
    }

    public AdopterEntity toEntity() {
        AdopterEntity entity = new AdopterEntity();
        entity.setId(this.getId());
        this.copyToEntity(entity);
        entity.setAddress(this.address);
        entity.setCity(this.city);
        entity.setHousingType(this.housingType);
        entity.setStatus(this.status);
        return entity;
    }
}
