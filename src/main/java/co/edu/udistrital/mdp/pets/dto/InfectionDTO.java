package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.InfectionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class InfectionDTO {
    private Long id;
    private String infectionType;
    private String description;
    private String treatment;
    private java.time.LocalDate diagnosisDate;
    private java.time.LocalDate recoveryDate;
    private Long medicalHistoryId;

    public InfectionDTO(InfectionEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.infectionType = entity.getInfectionType();
            this.description = entity.getDescription();
            this.treatment = entity.getTreatment();
            this.diagnosisDate = entity.getDiagnosisDate();
            this.recoveryDate = entity.getRecoveryDate();
            if (entity.getMedicalHistory() != null) {
                this.medicalHistoryId = entity.getMedicalHistory().getId();
            }
        }
    }

    public InfectionEntity toEntity() {
        InfectionEntity entity = new InfectionEntity();
        entity.setId(this.id);
        entity.setInfectionType(this.infectionType);
        entity.setDescription(this.description);
        entity.setTreatment(this.treatment);
        entity.setDiagnosisDate(this.diagnosisDate);
        entity.setRecoveryDate(this.recoveryDate);
        return entity;
    }
}
