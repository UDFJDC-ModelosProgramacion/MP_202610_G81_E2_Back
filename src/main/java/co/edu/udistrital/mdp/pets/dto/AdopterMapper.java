package co.edu.udistrital.mdp.pets.dto;

import org.mapstruct.Mapper;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;

@Mapper(componentModel = "spring")
public interface AdopterMapper {
          AdopterCreationDTO entityToDTO(AdopterEntity entity);

          AdopterEntity dtoToEntity(AdopterCreationDTO dto);

          AdopterDTO CdtoTodto(AdopterCreationDTO cdto);
}