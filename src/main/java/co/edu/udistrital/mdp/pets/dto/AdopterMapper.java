package co.edu.udistrital.mdp.pets.dto;

import org.mapstruct.Mapper;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;

@Mapper(componentModel = "spring")
public interface AdopterMapper {
          AdopterDTO entityToDTO(AdopterEntity entity);

          AdopterEntity dtoToEntity(AdopterCreationDTO dto);
}