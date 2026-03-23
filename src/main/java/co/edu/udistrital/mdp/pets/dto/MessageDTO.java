package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for MessageEntity.
 * Prevents circular reference by using only sender/receiver IDs instead of full UserDTOs.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessageDTO {
    private Long id;
    private String content;
    private LocalDateTime sentAt;
    private Boolean isRead;
    private Boolean active;
    
    // Use IDs instead of full objects to break cycles
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
}
