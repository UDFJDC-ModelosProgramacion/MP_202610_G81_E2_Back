package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class UserEntity extends BaseEntity {

    private String name;
    private String email;
    private String password;
    private String phoneNumber;
    private String profileImageUrl;
    private LocalDateTime registerDate;

    @OneToMany(mappedBy = "sender")
    @JsonIgnore
    private List<MessageEntity> sentMessages = new ArrayList<>();

    @OneToMany(mappedBy = "receiver")
    @JsonIgnore
    private List<MessageEntity> receivedMessages = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<NotificationEntity> notifications = new ArrayList<>();
}