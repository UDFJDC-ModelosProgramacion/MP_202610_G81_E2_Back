package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ReturnCaseDTO {
    private Long id;
    private LocalDate returnDate;
    private String reason;
    private String details;
    private Long adoptionProcessId;
}
