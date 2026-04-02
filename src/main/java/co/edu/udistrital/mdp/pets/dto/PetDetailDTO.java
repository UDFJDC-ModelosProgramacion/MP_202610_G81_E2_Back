package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Detail DTO for a Pet, including medical history and adoption request IDs.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PetDetailDTO extends PetDTO {
    private MedicalHistoryDTO medicalHistory;
    private List<Long> adoptionRequestIds = new ArrayList<>();

    public PetDetailDTO(PetEntity entity) {
        super(entity);
        if (entity != null) {
            if (entity.getMedicalHistory() != null) {
                this.medicalHistory = new MedicalHistoryDTO(entity.getMedicalHistory());
            }
            if (entity.getAdoptionRequests() != null) {
                entity.getAdoptionRequests().forEach(req -> adoptionRequestIds.add(req.getId()));
            }
        }
    }
}
