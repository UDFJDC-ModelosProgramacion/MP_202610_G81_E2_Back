package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.MedicationEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MedicationDTO {
    private Long id;
    private String medicationName;
    private String dosage;
    private String frequency;
    private java.time.LocalDate startDate;
    private java.time.LocalDate endDate;
    private String notes;
    private Long medicalHistoryId;

    public MedicationDTO(MedicationEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.medicationName = entity.getMedicationName();
            this.dosage = entity.getDosage();
            this.frequency = entity.getFrequency();
            this.startDate = entity.getStartDate();
            this.endDate = entity.getEndDate();
            this.notes = entity.getNotes();
            if (entity.getMedicalHistory() != null) {
                this.medicalHistoryId = entity.getMedicalHistory().getId();
            }
        }
    }

    public MedicationEntity toEntity() {
        MedicationEntity entity = new MedicationEntity();
        entity.setId(this.id);
        entity.setMedicationName(this.medicationName);
        entity.setDosage(this.dosage);
        entity.setFrequency(this.frequency);
        entity.setStartDate(this.startDate);
        entity.setEndDate(this.endDate);
        entity.setNotes(this.notes);
        return entity;
    }
}
