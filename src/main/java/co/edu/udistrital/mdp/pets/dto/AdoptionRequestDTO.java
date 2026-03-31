package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AdoptionRequestDTO {
    private LocalDate requestDate;
    private String status;
    private String comments;
}
