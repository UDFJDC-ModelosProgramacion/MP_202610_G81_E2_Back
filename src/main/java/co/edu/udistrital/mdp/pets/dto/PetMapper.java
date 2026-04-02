package co.edu.udistrital.mdp.pets.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.udistrital.mdp.pets.entities.PetEntity;

@Mapper(componentModel = "spring", uses = { ShelterMapper.class })
public interface PetMapper {

          PetCreationDTO entityToDTO(PetEntity entity);

          @Mapping(source = "shelterId", target = "shelter.id")
          @Mapping(target = "medicalHistory", ignore = true)
          PetEntity dtoToEntity(PetCreationDTO dto);

          
}
