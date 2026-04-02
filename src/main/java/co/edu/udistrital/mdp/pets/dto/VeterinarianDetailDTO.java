package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class VeterinarianDetailDTO extends VeterinarianDTO {
    private List<SpecialtiesDTO> specialities = new ArrayList<>();
    private List<Long> medicalHistoryIds = new ArrayList<>();

    public VeterinarianDetailDTO(VeterinarianEntity entity) {
        super(entity);
        if (entity != null) {
            if (entity.getSpecialities() != null) {
                entity.getSpecialities().forEach(s -> specialities.add(new SpecialtiesDTO(s)));
            }
            if (entity.getMedicalHistories() != null) {
                entity.getMedicalHistories().forEach(h -> medicalHistoryIds.add(h.getId()));
            }
        }
    }
}
