package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.VaccineEntryEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class VaccineDTO {
    private Long id;
    private String vaccineName;
    private LocalDate adminDate;
    private LocalDate nextDueDate;
    private Long medicalHistoryId;

    public VaccineDTO(VaccineEntryEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.vaccineName = entity.getVaccineName();
            this.adminDate = entity.getAdminDate();
            this.nextDueDate = entity.getNextDueDate();
            if (entity.getMedicalHistory() != null) {
                this.medicalHistoryId = entity.getMedicalHistory().getId();
            }
        }
    }

    public VaccineEntryEntity toEntity() {
        VaccineEntryEntity entity = new VaccineEntryEntity();
        entity.setId(this.id);
        entity.setVaccineName(this.vaccineName);
        entity.setAdminDate(this.adminDate);
        entity.setNextDueDate(this.nextDueDate);
        return entity;
    }
}
