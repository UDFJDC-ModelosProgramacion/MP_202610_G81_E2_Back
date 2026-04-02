package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.PetCreationDTO;
import co.edu.udistrital.mdp.pets.dto.PetMapper;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.services.PetService;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;


@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    @Autowired
    private PetMapper mapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<PetCreationDTO> findAll() {
        return petService.searchPets()
                .stream()
                .map(mapper::entityToDTO)
                .toList();
    }

    @GetMapping("/{petId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PetCreationDTO findOne(@PathVariable Long petId) throws EntityNotFoundException {
        return mapper.entityToDTO(petService.getPet(petId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public PetCreationDTO create(@RequestBody PetCreationDTO dto)
            throws IllegalOperationException {

        petService.createPet(mapper.dtoToEntity(dto).getShelter().getId(), mapper.dtoToEntity(dto));
        return dto;
    }

    @PutMapping("/{petId}")
    @ResponseStatus(code = HttpStatus.OK)
    public PetCreationDTO update(@PathVariable Long petId,
            @RequestBody PetCreationDTO dto)
            throws EntityNotFoundException, IllegalOperationException {

        return mapper.entityToDTO(
                petService.updatePet(petId, mapper.dtoToEntity(dto))
        );
    }

    @DeleteMapping("/{petId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long petId)
            throws EntityNotFoundException, IllegalOperationException {

        petService.deletePet(petId);
    }
}
}