package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class AdopterDetailDTO extends AdopterDTO {
    private List<RelationSummaryDTO> adoptions = new ArrayList<>();
    private List<RelationSummaryDTO> adoptionRequests = new ArrayList<>();
    private List<RelationSummaryDTO> reviews = new ArrayList<>();
}
