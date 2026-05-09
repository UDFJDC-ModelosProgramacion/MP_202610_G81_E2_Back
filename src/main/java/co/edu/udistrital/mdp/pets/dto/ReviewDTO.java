package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDTO {

    /*
     * =========================
     * Datos propios de Review
     * =========================
     */
    private Long id;
    private Integer rating;
    private String comment;
    private Boolean isSuccessStory;
    private LocalDate reviewDate;

    /*
     * =========================
     * Adoptante
     * =========================
     */
    private Long adopterId;
    private String adopterName;

    /*
     * =========================
     * Refugio
     * =========================
     */
    private Long shelterId;
    private String shelterName;

    /*
     * =========================
     * Proceso de adopción
     * =========================
     */
    private Long adoptionProcessId;

    /*
     * =========================
     * Mascota
     * =========================
     */
    private Long petId;
    private String petName;
    private String breed;

    public ReviewDTO(ReviewEntity entity) {
        if (entity == null)
            return;

        /* Datos Review */
        this.id = entity.getId();
        this.rating = entity.getRating();
        this.comment = entity.getComment();
        this.isSuccessStory = entity.getIsSuccessStory();
        this.reviewDate = entity.getReviewDate();

        /* Adoptante */
        if (entity.getAdopter() != null) {
            this.adopterId = entity.getAdopter().getId();
            this.adopterName = entity.getAdopter().getName();
        }

        /* Refugio */
        if (entity.getShelter() != null) {
            this.shelterId = entity.getShelter().getId();
            this.shelterName = entity.getShelter().getShelterName();
        }

        /* Proceso adopción + Mascota */
        if (entity.getAdoptionProcess() != null) {
            this.adoptionProcessId = entity.getAdoptionProcess().getId();

            if (entity.getAdoptionProcess().getPet() != null) {
                PetEntity pet = entity.getAdoptionProcess().getPet();

                this.petId = pet.getId();
                this.petName = pet.getName();
                this.breed = pet.getBreed();
            }
        }
    }

    public ReviewEntity toEntity() {
        ReviewEntity entity = new ReviewEntity();

        /* Datos Review */
        entity.setId(this.id);
        entity.setRating(this.rating);
        entity.setComment(this.comment);
        entity.setIsSuccessStory(this.isSuccessStory);
        entity.setReviewDate(this.reviewDate);

        /* Adoptante */
        if (this.adopterId != null) {
            AdopterEntity adopter = new AdopterEntity();
            adopter.setId(this.adopterId);
            entity.setAdopter(adopter);
        }

        /* Refugio */
        if (this.shelterId != null) {
            ShelterEntity shelter = new ShelterEntity();
            shelter.setId(this.shelterId);
            entity.setShelter(shelter);
        }

        return entity;
    }
}