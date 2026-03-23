package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Base DTO for UserEntity (AdopterEntity, VeterinarianEntity).
 * Excludes bidirectional references to prevent JSON serialization loops.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private String profileImageUrl;
    private LocalDateTime registerDate;
    
    // Counts instead of full lists to avoid cycles
    private Integer sentMessagesCount = 0;
    private Integer receivedMessagesCount = 0;
    private Integer notificationsCount = 0;
}
