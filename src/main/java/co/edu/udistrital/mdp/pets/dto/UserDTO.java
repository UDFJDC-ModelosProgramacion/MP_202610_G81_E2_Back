package co.edu.udistrital.mdp.pets.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.edu.udistrital.mdp.pets.entities.UserEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String name;
    private String email;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;
    private String phoneNumber;
    private String profileImageUrl;
    private LocalDateTime registerDate;

    public UserDTO(UserEntity entity) {
        if (entity != null) {
            this.id = entity.getId();
            this.name = entity.getName();
            this.email = entity.getEmail();
            this.phoneNumber = entity.getPhoneNumber();
            this.profileImageUrl = entity.getProfileImageUrl();
            this.registerDate = entity.getRegisterDate();
        }
    }

    public void copyToEntity(UserEntity entity) {
        entity.setName(this.name);
        entity.setEmail(this.email);
        entity.setPassword(this.password);
        entity.setPhoneNumber(this.phoneNumber);
        entity.setProfileImageUrl(this.profileImageUrl);
        entity.setRegisterDate(this.registerDate);
    }
}
