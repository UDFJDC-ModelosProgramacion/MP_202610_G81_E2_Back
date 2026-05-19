package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Detail DTO for a Pet, including full medical history (vaccines + events),
 * shelter information, and adoption request IDs.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PetDetailDTO extends PetDTO {
    private MedicalHistoryDetailDTO medicalHistory;
    private ShelterDTO shelter;
    private List<Long> adoptionRequestIds = new ArrayList<>();

    public PetDetailDTO(PetEntity entity) {
        super(entity);
        if (entity != null) {
            if (entity.getMedicalHistory() != null) {
                this.medicalHistory = new MedicalHistoryDetailDTO(entity.getMedicalHistory());
            }
            if (entity.getShelter() != null) {
                this.shelter = new ShelterDTO(entity.getShelter());
            }
            if (entity.getAdoptionRequests() != null) {
                entity.getAdoptionRequests().forEach(req -> adoptionRequestIds.add(req.getId()));
            }
        }
    }
}
