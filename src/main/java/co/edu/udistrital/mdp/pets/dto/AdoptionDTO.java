package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdoptionDTO {
    private Long id;
    private LocalDate officialDate;
    private Boolean contractSigned;
    private String status;
    private Long adopterId;
    private Long petId;
    private Long shelterId;

    public AdoptionDTO(AdoptionEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.officialDate = entity.getOfficialDate();
            this.contractSigned = entity.getContractSigned();
            this.status = entity.getStatus() != null ? entity.getStatus().name() : null;
            if (entity.getAdopter() != null) this.adopterId = entity.getAdopter().getId();
            if (entity.getPet() != null) this.petId = entity.getPet().getId();
            if (entity.getShelter() != null) this.shelterId = entity.getShelter().getId();
        }
    }

    public AdoptionEntity toEntity() {
        AdoptionEntity entity = new AdoptionEntity();
        entity.setId(this.id);
        entity.setOfficialDate(this.officialDate);
        entity.setContractSigned(this.contractSigned);
        return entity;
    }
}
