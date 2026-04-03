package co.edu.udistrital.mdp.pets.dto;

import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NotificationDetailDTO extends NotificationDTO {
    public NotificationDetailDTO(NotificationEntity entity) {
        super(entity);
    }
}
