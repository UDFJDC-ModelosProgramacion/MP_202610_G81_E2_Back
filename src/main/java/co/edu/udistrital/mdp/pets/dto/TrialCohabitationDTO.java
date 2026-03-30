package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class TrialCohabitationDTO extends AdoptionProcessDTO {
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean approved;
    private String notes;

    public TrialCohabitationDTO(TrialCohabitationEntity entity) {
        super(entity);
        if (entity != null) {
            this.startDate = entity.getStartDate();
            this.endDate = entity.getEndDate();
            this.approved = entity.getApproved();
            this.notes = entity.getNotes();
        }
    }

    public TrialCohabitationEntity toTrialEntity() {
        TrialCohabitationEntity entity = new TrialCohabitationEntity();
        entity.setId(this.getId());
        entity.setCreationDate(this.getCreationDate());
        entity.setStatus(this.getStatus());
        entity.setStartDate(this.startDate);
        entity.setEndDate(this.endDate);
        entity.setApproved(this.approved);
        entity.setNotes(this.notes);
        return entity;
    }
}
