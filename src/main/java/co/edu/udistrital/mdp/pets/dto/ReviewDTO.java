package co.edu.udistrital.mdp.pets.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReviewDTO {
    private Long id;
    private Integer rating;
    private String comment;
    private Boolean isSuccessStory;
    private Long adopterId;
    private Long adoptionProcessId;
    private Long shelterId;
}
