package co.edu.udistrital.mdp.pets.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;

@Mapper(componentModel = "spring")
public interface MedicalHistoryMapper {
          @Mapping(source = "pet.id", target = "petId")
          MedicalHistoryDTO entityToDTO(MedicalHistoryEntity entity);
}
