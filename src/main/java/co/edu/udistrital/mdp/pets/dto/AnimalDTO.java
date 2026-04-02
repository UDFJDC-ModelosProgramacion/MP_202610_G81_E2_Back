package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO representing a generic Animal (base data for PetEntity).
 * Maps to PetEntity's core bio fields.
 */
@Data
@NoArgsConstructor
public class AnimalDTO {
    private Long id;
    private String name;
    private String breed;
    private LocalDate bornDate;
    private String sex;
    private String size;
    private String temperament;
    private String specificNeeds;
    private Boolean isRescued;
    private String status;

    public AnimalDTO(PetEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.breed = entity.getBreed();
            this.bornDate = entity.getBornDate();
            this.sex = entity.getSex();
            this.size = entity.getSize();
            this.temperament = entity.getTemperament();
            this.specificNeeds = entity.getSpecificNeeds();
            this.isRescued = entity.getIsRescued();
            this.status = entity.getStatus();
        }
    }
}
