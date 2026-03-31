package co.edu.udistrital.mdp.pets.dto;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;

@Mapper(componentModel = "spring", uses = { MedicalEventMapper.class, VaccineEntryMapper.class })
public interface MedicalHistoryMapper {

          @Mapping(target = "petId", ignore = true)
          MedicalHistoryCreationDTO entityToDTO(MedicalHistoryEntity entity);
}
