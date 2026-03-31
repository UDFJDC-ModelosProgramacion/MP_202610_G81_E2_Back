package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.AdoptionDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.mappers.EntityDtoMapper;
import co.edu.udistrital.mdp.pets.services.AdoptionService;

import java.util.List;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {
    
    @Autowired
    private AdoptionService adoptionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionDTO createAdoption(@RequestBody AdoptionDTO adoptionDto)
            throws IllegalOperationException {
        AdoptionEntity saved = adoptionService.createAdoption(EntityDtoMapper.toEntity(adoptionDto));
        return EntityDtoMapper.toDto(saved);
    }

    @GetMapping
    public List<AdoptionDTO> getAdoptions() {
        return EntityDtoMapper.toAdoptionDtoList(adoptionService.searchAdoptions());
    }

    @GetMapping("/{id}")
    public AdoptionDTO getAdoptionById(@PathVariable Long id)
            throws EntityNotFoundException {
        return EntityDtoMapper.toDto(adoptionService.searchAdoption(id));
    }

    @PutMapping("/{id}")
    public AdoptionDTO updateAdoption(@PathVariable Long id,
                                        @RequestBody AdoptionDTO adoptionDto)
            throws EntityNotFoundException, IllegalOperationException {
        AdoptionEntity updated = adoptionService.updateAdoption(id, EntityDtoMapper.toEntity(adoptionDto));
        return EntityDtoMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdoption(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        adoptionService.deleteAdoption(id);
    }

    @PostMapping("/{id}/request")
    public AdoptionDTO requestAdoption(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        return EntityDtoMapper.toDto(adoptionService.requestAdoption(id));
    }

    @PostMapping("/{id}/approve")
    public AdoptionDTO approveAdoption(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        return EntityDtoMapper.toDto(adoptionService.approveAdoption(id));
    }

    @PostMapping("/{id}/trial")
    public AdoptionDTO trialCohabitation(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        return EntityDtoMapper.toDto(adoptionService.startTrial(id, null));
    }

    @PostMapping("/{id}/return")
    public AdoptionDTO returnPet(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        return EntityDtoMapper.toDto(adoptionService.returnPet(id));
    }
}