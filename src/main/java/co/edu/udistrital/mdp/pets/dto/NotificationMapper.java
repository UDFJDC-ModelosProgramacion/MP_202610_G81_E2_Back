package co.edu.udistrital.mdp.pets.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.udistrital.mdp.pets.entities.NotificationEntity;

@Mapper(componentModel = "spring")
public interface NotificationMapper {

          @Mapping(source = "id", target = "id")
          NotificationDTO toDTO(NotificationEntity notification);
}
