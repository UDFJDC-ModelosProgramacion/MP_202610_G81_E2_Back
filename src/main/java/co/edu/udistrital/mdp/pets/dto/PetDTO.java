package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a Pet (mascota), extending the Animal concept with shelter and adoption context.
 */
@Data
@NoArgsConstructor
public class PetDTO {
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
    private Long shelterId;

    public PetDTO(PetEntity entity) {
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
            if (entity.getShelter() != null) {
                this.shelterId = entity.getShelter().getId();
            }
        }
    }

    public PetEntity toEntity() {
        PetEntity entity = new PetEntity();
        entity.setId(this.id);
        entity.setName(this.name);
        entity.setBreed(this.breed);
        entity.setBornDate(this.bornDate);
        entity.setSex(this.sex);
        entity.setSize(this.size);
        entity.setTemperament(this.temperament);
        entity.setSpecificNeeds(this.specificNeeds);
        entity.setIsRescued(this.isRescued);
        entity.setStatus(this.status);
        return entity;
    }
}
