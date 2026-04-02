package co.edu.udistrital.mdp.pets.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;

@Mapper(componentModel = "spring", uses = {PetMapper.class, AdopterMapper.class})
public interface AdoptionRequestMapper {

    AdoptionRequestCreationDTO entityToDTO(AdoptionRequestEntity entity);

    AdoptionRequestEntity dtoToEntity(AdoptionRequestCreationDTO dto);

    AdoptionRequestCreationDTO CdtoTodto(AdoptionRequestDTO entityToDTO);
}