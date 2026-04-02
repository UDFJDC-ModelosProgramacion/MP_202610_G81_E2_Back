package co.edu.udistrital.mdp.pets.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;

@Mapper(componentModel = "spring", uses = {PetMapper.class, AdopterMapper.class})
public interface AdoptionMapper {

    AdoptionCreationDTO entityToDTO(AdoptionEntity entity);

    @Mapping(source = "petId", target = "pet.id")
    @Mapping(source = "adopterId", target = "adopter.id")
    AdoptionEntity dtoToEntity(AdoptionCreationDTO dto);

    AdoptionDTO CdtoTodto(AdoptionCreationDTO dto);
}          