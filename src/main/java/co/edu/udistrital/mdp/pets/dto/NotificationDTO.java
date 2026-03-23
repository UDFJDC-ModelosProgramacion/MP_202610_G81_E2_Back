package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for NotificationEntity.
 * Uses user ID instead of full UserDTO to prevent cycles.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isRead;
    
    // Use ID instead of full object to break cycles
    private Long userId;
    private String userName;
}
