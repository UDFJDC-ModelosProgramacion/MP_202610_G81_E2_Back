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
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.mappers.EntityDtoMapper;
import co.edu.udistrital.mdp.pets.services.VeterinarianService;

@RestController
@RequestMapping("/veterinarians")
public class VeterinarianController {

    @Autowired
    private VeterinarianService veterinarianService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VeterinarianDTO createVeterinarian(@RequestBody VeterinarianDTO vetDto) throws IllegalOperationException {
        VeterinarianEntity saved = veterinarianService.createVeterinarian(EntityDtoMapper.toEntity(vetDto));
        return EntityDtoMapper.toDto(saved);
    }

    @GetMapping
    public List<VeterinarianDTO> getVeterinarians(
            @RequestParam(required = false) Long shelterId,
            @RequestParam(required = false) Long specialityId) {
        if (specialityId != null) {
            return EntityDtoMapper.toVeterinarianDtoList(veterinarianService.searchVeterinariansBySpeciality(specialityId));
        }
        if (shelterId != null) {
            return EntityDtoMapper.toVeterinarianDtoList(veterinarianService.searchVeterinariansByShelter(shelterId));
        }
        return EntityDtoMapper.toVeterinarianDtoList(veterinarianService.searchVeterinarians());
    }

    @GetMapping("/{id}")
    public VeterinarianDTO getVeterinarianById(@PathVariable Long id) throws EntityNotFoundException {
        return EntityDtoMapper.toDto(veterinarianService.searchVeterinarian(id));
    }

    @GetMapping("/{id}/detail")
    public VeterinarianDetailDTO getVeterinarianDetail(@PathVariable Long id) throws EntityNotFoundException {
        return EntityDtoMapper.toDetailDto(veterinarianService.searchVeterinarian(id));
    }

    @PutMapping("/{id}")
    public VeterinarianDTO updateVeterinarian(@PathVariable Long id, @RequestBody VeterinarianDTO updatedVet)
            throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity updated = veterinarianService.updateVeterinarian(id, EntityDtoMapper.toEntity(updatedVet));
        return EntityDtoMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVeterinarian(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException {
        veterinarianService.deleteVeterinarian(id);
    }
}