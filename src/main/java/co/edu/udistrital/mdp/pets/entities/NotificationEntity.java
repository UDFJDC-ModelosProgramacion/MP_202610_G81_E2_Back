package co.edu.udistrital.mdp.pets.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;
import uk.co.jemos.podam.common.PodamExclude;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class NotificationEntity extends BaseEntity {

    private String title;
    private String content;
    private LocalDateTime createdAt;
    private Boolean isRead;

    @PodamExclude
    @ManyToOne
    private UserEntity user;
}