package co.edu.udistrital.mdp.pets.dto;

import org.mapstruct.Mapper;

import co.edu.udistrital.mdp.pets.entities.MessageEntity;

@Mapper(componentModel = "spring")
public interface MessageMapper {

          MessageDTO entityToDTO(MessageEntity entity);
}