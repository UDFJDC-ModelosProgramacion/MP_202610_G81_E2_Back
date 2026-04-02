package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import co.edu.udistrital.mdp.pets.entities.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MessageDTO {
    private Long id;
    private String content;
    private LocalDateTime sentAt;
    private Boolean isRead;
    private Boolean active;
    private UserDTO sender;
    private UserDTO receiver;

    public MessageDTO(MessageEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.content = entity.getContent();
            this.sentAt = entity.getSentAt();
            this.isRead = entity.getIsRead();
            this.active = entity.getActive();
            if (entity.getSender() != null) {
                this.sender = new UserDTO(entity.getSender());
            }
            if (entity.getReceiver() != null) {
                this.receiver = new UserDTO(entity.getReceiver());
            }
        }
    }

    public MessageEntity toEntity() {
        MessageEntity entity = new MessageEntity();
        entity.setId(this.id);
        entity.setContent(this.content);
        entity.setSentAt(this.sentAt);
        entity.setIsRead(this.isRead);
        entity.setActive(this.active);
        if (this.sender != null && this.sender.getId() != null) {
            entity.setSender(buildUserReference(this.sender.getId()));
        }
        if (this.receiver != null && this.receiver.getId() != null) {
            entity.setReceiver(buildUserReference(this.receiver.getId()));
        }
        return entity;
    }

    private UserEntity buildUserReference(Long userId) {
        AdopterEntity entity = new AdopterEntity();
        entity.setId(userId);
        return entity;
    }
}
