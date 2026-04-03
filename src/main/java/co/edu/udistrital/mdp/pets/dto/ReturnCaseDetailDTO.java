package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.ReturnCaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ReturnCaseDetailDTO extends ReturnCaseDTO {
    public ReturnCaseDetailDTO(ReturnCaseEntity entity) {
        super(entity);
    }
}
