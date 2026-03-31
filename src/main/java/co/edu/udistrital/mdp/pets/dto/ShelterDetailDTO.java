package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ShelterDetailDTO extends ShelterDTO {
    private List<RelationSummaryDTO> veterinarians = new ArrayList<>();
    private List<RelationSummaryDTO> mediaItems = new ArrayList<>();
    private List<RelationSummaryDTO> reviews = new ArrayList<>();
    private List<RelationSummaryDTO> adoptions = new ArrayList<>();
}
