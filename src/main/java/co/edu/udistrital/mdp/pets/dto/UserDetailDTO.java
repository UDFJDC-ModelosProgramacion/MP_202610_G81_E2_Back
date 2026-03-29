package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.pets.entities.UserEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserDetailDTO extends UserDTO {
    private List<MessageDTO> sentMessages = new ArrayList<>();
    private List<MessageDTO> receivedMessages = new ArrayList<>();
    private List<NotificationDTO> notifications = new ArrayList<>();

    public UserDetailDTO(UserEntity entity) {
        super(entity);
        if (entity != null) {
            entity.getSentMessages().forEach(message -> sentMessages.add(new MessageDTO(message)));
            entity.getReceivedMessages().forEach(message -> receivedMessages.add(new MessageDTO(message)));
            entity.getNotifications().forEach(notification -> notifications.add(new NotificationDTO(notification)));
        }
    }
}
