package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserEntity extends BaseEntity {

    private String email;
    private String password;
    private String phoneNumber;
    private String profileImageUrl;
    private LocalDateTime registerDate;

    @OneToMany(mappedBy = "sender")
    private List<MessageEntity> sentMessages;

    @OneToMany(mappedBy = "receiver")
    private List<MessageEntity> receivedMessages;

    @OneToMany(mappedBy = "user")
    private List<NotificationEntity> notifications;
}
