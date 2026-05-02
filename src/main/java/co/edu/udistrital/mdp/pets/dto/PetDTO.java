package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for a Pet (mascota) with shelter and adoption context.
 */
@Data
@NoArgsConstructor
public class PetDTO {
    private Long id;
    
    @NotBlank(message = "Name is mandatory")
    private String name;
    
    @NotBlank(message = "Breed is mandatory")
    private String breed;
    
    private LocalDate bornDate;
    
    @NotBlank(message = "Sex is mandatory")
    private String sex;
    
    @NotBlank(message = "Size is mandatory")
    private String size;
    
    @NotBlank(message = "Temperament is mandatory")
    private String temperament;
    
    private String specificNeeds;
    private Boolean isRescued;
    private String status;
    
    @NotBlank(message = "Photo is mandatory")
    private String photo;
    
    @NotNull(message = "Shelter ID is mandatory")
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
            this.photo = entity.getPhoto();
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
        entity.setPhoto(this.photo);
        if (this.shelterId != null) {
            ShelterEntity shelter = new ShelterEntity();
            shelter.setId(this.shelterId);
            entity.setShelter(shelter);
        }
        return entity;
    }
}
