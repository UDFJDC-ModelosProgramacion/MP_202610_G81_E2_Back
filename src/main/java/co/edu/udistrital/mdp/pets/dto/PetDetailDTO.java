package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class PetDetailDTO extends PetDTO {
    private List<RelationSummaryDTO> adoptionRequests = new ArrayList<>();
    private List<RelationSummaryDTO> adoptions = new ArrayList<>();
    private List<RelationSummaryDTO> medicalEvents = new ArrayList<>();
    private List<RelationSummaryDTO> vaccines = new ArrayList<>();
}
