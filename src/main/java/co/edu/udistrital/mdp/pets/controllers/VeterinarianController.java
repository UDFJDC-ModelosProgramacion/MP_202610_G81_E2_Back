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

import co.edu.udistrital.mdp.pets.dto.VeterinarianDTO;
import co.edu.udistrital.mdp.pets.dto.VeterinarianDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.VeterinarianService;

@RestController
@RequestMapping("/veterinarians")
public class VeterinarianController {

    @Autowired
    private VeterinarianService veterinarianService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VeterinarianDTO> findAll(
            @RequestParam(required = false) Long specialityId,
            @RequestParam(required = false) Long shelterId) {
        List<?> vets;
        if (specialityId != null) {
            vets = veterinarianService.searchVeterinariansBySpeciality(specialityId);
        } else if (shelterId != null) {
            vets = veterinarianService.searchVeterinariansByShelter(shelterId);
        } else {
            vets = veterinarianService.searchVeterinarians();
        }
        return vets.stream()
                .map(e -> new VeterinarianDTO(
                        (co.edu.udistrital.mdp.pets.entities.VeterinarianEntity) e))
                .toList();
    }

    @GetMapping(value = "/{vetId}")
    @ResponseStatus(code = HttpStatus.OK)
    public VeterinarianDetailDTO findOne(@PathVariable Long vetId) throws EntityNotFoundException {
        return new VeterinarianDetailDTO(veterinarianService.searchVeterinarian(vetId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public VeterinarianDetailDTO create(@RequestBody VeterinarianDTO vetDTO)
            throws IllegalOperationException, EntityNotFoundException {
        return new VeterinarianDetailDTO(
                veterinarianService.createVeterinarian(vetDTO.toEntity()));
    }

    @PutMapping(value = "/{vetId}")
    @ResponseStatus(code = HttpStatus.OK)
    public VeterinarianDetailDTO update(@PathVariable Long vetId,
            @RequestBody VeterinarianDTO vetDTO)
            throws EntityNotFoundException, IllegalOperationException {
        return new VeterinarianDetailDTO(
                veterinarianService.updateVeterinarian(vetId, vetDTO.toEntity()));
    }

    @DeleteMapping(value = "/{vetId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long vetId)
            throws EntityNotFoundException, IllegalOperationException {
        veterinarianService.deleteVeterinarian(vetId);
    }
}