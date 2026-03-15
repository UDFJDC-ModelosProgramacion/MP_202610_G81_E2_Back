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
public class MessageEntity extends BaseEntity {

    private String content;
    private LocalDateTime sentAt;
    private Boolean isRead;
    private Boolean active = true;

    @PodamExclude
    @ManyToOne
    private UserEntity sender;

    @PodamExclude
    @ManyToOne
    private UserEntity receiver;
}