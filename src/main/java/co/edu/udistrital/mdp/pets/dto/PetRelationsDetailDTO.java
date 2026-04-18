package co.edu.udistrital.mdp.pets.dto;

import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PetRelationsDetailDTO {
    private List<Map<String, Object>> items;
}
