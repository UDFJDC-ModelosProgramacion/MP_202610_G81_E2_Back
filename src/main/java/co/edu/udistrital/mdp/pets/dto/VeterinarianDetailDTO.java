package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class VeterinarianDetailDTO extends VeterinarianDTO {
    private List<RelationSummaryDTO> specialities = new ArrayList<>();
    private List<RelationSummaryDTO> medicalHistories = new ArrayList<>();
    private List<RelationSummaryDTO> medicalEvents = new ArrayList<>();
}
