package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageDetailDTO extends MessageDTO {
    public MessageDetailDTO(MessageEntity entity) {
        super(entity);
    }
}
