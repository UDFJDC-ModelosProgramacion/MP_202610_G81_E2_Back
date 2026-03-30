package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.pets.entities.VetSpecialityEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SpecialtiesDetailDTO extends SpecialtiesDTO {
    private List<Long> veterinarianIds = new ArrayList<>();

    public SpecialtiesDetailDTO(VetSpecialityEntity entity) {
        super(entity);
        if (entity != null && entity.getVeterinarians() != null) {
            entity.getVeterinarians().forEach(vet -> veterinarianIds.add(vet.getId()));
        }
    }
}
