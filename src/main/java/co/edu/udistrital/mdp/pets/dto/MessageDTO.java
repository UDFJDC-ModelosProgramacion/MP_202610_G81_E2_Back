package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;

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

}