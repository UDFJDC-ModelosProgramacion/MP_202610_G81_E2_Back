package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.ReturnCaseEntity;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReturnCaseDTO {
    private Long id;
    private LocalDate returnDate;
    private String reason;
    private String details;
    private Long adoptionProcessId;

    public ReturnCaseDTO(ReturnCaseEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.returnDate = entity.getReturnDate();
            this.reason = entity.getReason();
            this.details = entity.getDetails();
            if (entity.getAdoptionProcess() != null) this.adoptionProcessId = entity.getAdoptionProcess().getId();
        }
    }

    public ReturnCaseEntity toEntity() {
        ReturnCaseEntity entity = new ReturnCaseEntity();
        entity.setId(this.id);
        entity.setReturnDate(this.returnDate);
        entity.setReason(this.reason);
        entity.setDetails(this.details);
        if (this.adoptionProcessId != null) {
            TrialCohabitationEntity adoptionProcess = new TrialCohabitationEntity();
            adoptionProcess.setId(this.adoptionProcessId);
            entity.setAdoptionProcess(adoptionProcess);
        }
        return entity;
    }
}
