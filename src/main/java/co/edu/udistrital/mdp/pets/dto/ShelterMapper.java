package co.edu.udistrital.mdp.pets.dto;

import org.mapstruct.Mapper;

@Mapper(componentModel = "Spring", uses = {
          VetMapper.class
})
public interface ShelterMapper {
          
}
