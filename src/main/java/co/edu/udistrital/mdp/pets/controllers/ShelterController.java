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

import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.ShelterService;

@RestController
@RequestMapping("/shelters")
public class ShelterController {

    @Autowired
    private ShelterService shelterService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ShelterDTO1> findAll(@RequestParam(required = false) String name) {
        List<?> shelters = (name != null)
                ? shelterService.searchSheltersByName(name)
                : shelterService.searchShelters();
        return shelters.stream()
                .map(e -> new ShelterDTO1((co.edu.udistrital.mdp.pets.entities.ShelterEntity) e))
                .toList();
    }

    @GetMapping(value = "/{shelterId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterDetailDTO findOne(@PathVariable Long shelterId) throws EntityNotFoundException {
        return new ShelterDetailDTO(shelterService.searchShelter(shelterId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ShelterDetailDTO create(@RequestBody ShelterDTO1 shelterDTO)
            throws IllegalOperationException {
        return new ShelterDetailDTO(shelterService.createShelter(shelterDTO.toEntity()));
    }

    @PutMapping(value = "/{shelterId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterDetailDTO update(@PathVariable Long shelterId,
            @RequestBody ShelterDTO1 shelterDTO)
            throws EntityNotFoundException, IllegalOperationException {
        return new ShelterDetailDTO(shelterService.updateShelter(shelterId, shelterDTO.toEntity()));
    }

    @DeleteMapping(value = "/{shelterId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long shelterId)
            throws EntityNotFoundException, IllegalOperationException {
        shelterService.deleteShelter(shelterId);
    }
}