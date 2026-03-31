package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdopterDTO extends UserDTO {
    private String address;
    private String city;
    private String housingType;
    private String status;

}
