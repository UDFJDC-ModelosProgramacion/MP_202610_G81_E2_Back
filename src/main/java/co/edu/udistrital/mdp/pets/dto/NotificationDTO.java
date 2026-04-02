package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.entities.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class NotificationDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isRead;
    private UserDTO user;

    public NotificationDTO(NotificationEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.title = entity.getTitle();
            this.content = entity.getContent();
            this.createdAt = entity.getCreatedAt();
            this.isRead = entity.getIsRead();
            if (entity.getUser() != null) {
                this.user = new UserDTO(entity.getUser());
            }
        }
    }

    public NotificationEntity toEntity() {
        NotificationEntity entity = new NotificationEntity();
        entity.setId(this.id);
        entity.setTitle(this.title);
        entity.setContent(this.content);
        entity.setCreatedAt(this.createdAt);
        entity.setIsRead(this.isRead);
        if (this.user != null && this.user.getId() != null) {
            entity.setUser(buildUserReference(this.user.getId()));
        }
        return entity;
    }

    private UserEntity buildUserReference(Long userId) {
        AdopterEntity entity = new AdopterEntity();
        entity.setId(userId);
        return entity;
    }
}
