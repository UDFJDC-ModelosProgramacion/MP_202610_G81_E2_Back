package co.edu.udistrital.mdp.pets.dto;

import java.util.ArrayList;
import java.util.List;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class AdopterDetailDTO extends AdopterDTO {
    private List<MessageDTO> sentMessages = new ArrayList<>();
    private List<MessageDTO> receivedMessages = new ArrayList<>();
    private List<NotificationDTO> notifications = new ArrayList<>();
    private List<Long> adoptionRequestIds = new ArrayList<>();
    private List<Long> reviewIds = new ArrayList<>();

    public AdopterDetailDTO(AdopterEntity entity) {
        super(entity);
        if (entity != null) {
            entity.getSentMessages().forEach(message -> sentMessages.add(new MessageDTO(message)));
            entity.getReceivedMessages().forEach(message -> receivedMessages.add(new MessageDTO(message)));
            entity.getNotifications().forEach(notification -> notifications.add(new NotificationDTO(notification)));
            entity.getAdoptionRequests().forEach(request -> adoptionRequestIds.add(request.getId()));
            entity.getReviews().forEach(review -> reviewIds.add(review.getId()));
        }
    }
}
