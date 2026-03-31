package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;

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
}
