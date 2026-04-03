package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MedicalHistoryDetailDTO extends MedicalHistoryDTO {
    private List<MedicalEventDTO> medicalEvents = new ArrayList<>();
    private List<VaccineDTO> vaccineEntries = new ArrayList<>();

    public MedicalHistoryDetailDTO(MedicalHistoryEntity entity) {
        super(entity);
        if (entity != null) {
            if (entity.getMedicalEvents() != null) {
                entity.getMedicalEvents().forEach(event -> medicalEvents.add(new MedicalEventDTO(event)));
            }
            if (entity.getVaccineEntries() != null) {
                entity.getVaccineEntries().forEach(vaccine -> vaccineEntries.add(new VaccineDTO(vaccine)));
            }
        }
    }
}
