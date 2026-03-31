package co.edu.udistrital.mdp.pets.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;

@Mapper(componentModel = "spring")
public interface ShelterMapper {

    ShelterDTO entityToDTO(ShelterEntity entity);

    ShelterEntity dtoToEntity(ShelterCreationDTO dto);
}
