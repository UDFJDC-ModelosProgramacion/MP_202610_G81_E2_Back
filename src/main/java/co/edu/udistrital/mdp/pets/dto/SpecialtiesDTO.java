package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.VetSpecialityEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SpecialtiesDTO {
    private Long id;
    private String name;
    private String description;

    public SpecialtiesDTO(VetSpecialityEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.description = entity.getDescription();
        }
    }

    public VetSpecialityEntity toEntity() {
        VetSpecialityEntity entity = new VetSpecialityEntity();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setDescription(this.description);
        return entity;
    }
}
