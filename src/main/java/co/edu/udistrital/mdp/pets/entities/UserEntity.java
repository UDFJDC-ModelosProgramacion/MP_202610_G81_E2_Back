package co.edu.udistrital.mdp.pets.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@EqualsAndHashCode(callSuper = true)
public abstract class UserEntity extends BaseEntity {

    private String name;
    private String email;
    private String password;
    private String phone;

    @PodamExclude
    @OneToMany(mappedBy = "sender", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<MessageEntity> sentMessages = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "receiver", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<MessageEntity> receivedMessages = new ArrayList<>();

    @PodamExclude
    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST, orphanRemoval = false)
    private List<NotificationEntity> notifications = new ArrayList<>();
}