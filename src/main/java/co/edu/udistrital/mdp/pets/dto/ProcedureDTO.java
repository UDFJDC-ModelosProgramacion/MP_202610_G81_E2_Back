package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.ProcedureEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProcedureDTO {
    private Long id;
    private String procedureName;
    private String description;
    private java.time.LocalDate procedureDate;
    private String outcome;
    private Long medicalHistoryId;
    private Long veterinarianId;

    public ProcedureDTO(ProcedureEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.procedureName = entity.getProcedureName();
            this.description = entity.getDescription();
            this.procedureDate = entity.getProcedureDate();
            this.outcome = entity.getOutcome();
            if (entity.getMedicalHistory() != null) {
                this.medicalHistoryId = entity.getMedicalHistory().getId();
            }
            if (entity.getVeterinarian() != null) {
                this.veterinarianId = entity.getVeterinarian().getId();
            }
        }
    }

    public ProcedureEntity toEntity() {
        ProcedureEntity entity = new ProcedureEntity();
        entity.setId(this.id);
        entity.setProcedureName(this.procedureName);
        entity.setDescription(this.description);
        entity.setProcedureDate(this.procedureDate);
        entity.setOutcome(this.outcome);
        return entity;
    }
}
