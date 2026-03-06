package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.management.Notification;

import org.apache.logging.log4j.message.Message;

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
    
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime registerDate;

    @OneToMany(mappedBy = "sender")
    private List<Message> setMessages;

    @OneToMany(mappedBy = "reciver")
    private List<Message> recivedMessages;

    @OneToMany(mappedBy = "user")
    private List<Notification> notifications;

    @OneToMany(mappedBy = "user")
    private List<ReviewEntity> reviews; 
}
