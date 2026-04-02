package co.edu.udistrital.mdp.pets.dto;

import org.mapstruct.Mapper;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;

@Mapper(componentModel = "spring", uses = {SpecialityMapper.class})
public interface VeterinarianMapper {
    VeterinarianDTO entityToDTO(VeterinarianEntity entity);
}
