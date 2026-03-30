package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.AnimalDTO;
import co.edu.udistrital.mdp.pets.dto.AnimalDetailDTO;
import co.edu.udistrital.mdp.pets.dto.PetDTO;
import co.edu.udistrital.mdp.pets.dto.PetDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.PetService;

@RestController
@RequestMapping("/animals")
public class AnimalController {

    @Autowired
    private PetService petService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AnimalDTO> findAll(
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String temperament) {
        return petService.searchPets(breed, size, temperament)
                .stream().map(AnimalDTO::new).toList();
    }

    @GetMapping(value = "/{animalId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AnimalDetailDTO findOne(@PathVariable Long animalId) throws EntityNotFoundException {
        return new AnimalDetailDTO(petService.getPet(animalId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AnimalDetailDTO create(@RequestParam Long shelterId,
            @RequestBody PetDTO petDTO) throws IllegalOperationException, EntityNotFoundException {
        return new AnimalDetailDTO(petService.createPet(shelterId, petDTO.toEntity()));
    }

    @PutMapping(value = "/{animalId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AnimalDetailDTO update(@PathVariable Long animalId,
            @RequestBody PetDTO petDTO) throws EntityNotFoundException {
        return new AnimalDetailDTO(petService.updatePet(animalId, petDTO.toEntity()));
    }

    @DeleteMapping(value = "/{animalId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long animalId)
            throws EntityNotFoundException, IllegalOperationException {
        petService.deletePet(animalId);
    }

    /**
     * Returns a PetDetailDTO view (with adoption requests) for the given animal.
     */
    @GetMapping(value = "/{animalId}/detail")
    @ResponseStatus(code = HttpStatus.OK)
    public PetDetailDTO findDetail(@PathVariable Long animalId) throws EntityNotFoundException {
        return new PetDetailDTO(petService.getPet(animalId));
    }
}
