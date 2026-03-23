package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.modelmapper.ModelMapper;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.dto.AdoptionProcessDTO;
import co.edu.udistrital.mdp.pets.dto.AdoptionRequestDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {
    
    @Autowired
    private AdoptionService adoptionService;
    
    @Autowired
    private ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionProcessDTO createAdoption(@RequestBody AdoptionRequestDTO adoptionRequest)
            throws IllegalOperationException {
        AdoptionEntity adoptionEntity = modelMapper.map(adoptionRequest, AdoptionEntity.class);
        AdoptionEntity created = adoptionService.createAdoption(adoptionEntity);
        return modelMapper.map(created, AdoptionProcessDTO.class);
    }

    @GetMapping
    public List<AdoptionProcessDTO> getAdoptions() {
        return adoptionService.searchAdoptions().stream()
                .map(adoption -> modelMapper.map(adoption, AdoptionProcessDTO.class))
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AdoptionProcessDTO getAdoptionById(@PathVariable Long id)
            throws EntityNotFoundException {
        AdoptionEntity adoption = adoptionService.searchAdoption(id);
        return modelMapper.map(adoption, AdoptionProcessDTO.class);
    }

    @PutMapping("/{id}")
    public AdoptionProcessDTO updateAdoption(@PathVariable Long id,
                                        @RequestBody AdoptionProcessDTO adoptionDTO)
            throws EntityNotFoundException, IllegalOperationException {
        AdoptionEntity adoptionEntity = modelMapper.map(adoptionDTO, AdoptionEntity.class);
        AdoptionEntity updated = adoptionService.updateAdoption(id, adoptionEntity);
        return modelMapper.map(updated, AdoptionProcessDTO.class);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdoption(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        adoptionService.deleteAdoption(id);
    }

    @PostMapping("/{id}/request")
    public AdoptionProcessDTO requestAdoption(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        AdoptionEntity adoption = adoptionService.requestAdoption(id);
        return modelMapper.map(adoption, AdoptionProcessDTO.class);
    }

    @PostMapping("/{id}/approve")
    public AdoptionProcessDTO approveAdoption(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        AdoptionEntity adoption = adoptionService.approveAdoption(id);
        return modelMapper.map(adoption, AdoptionProcessDTO.class);
    }

    @PostMapping("/{id}/trial")
    public AdoptionProcessDTO trialCohabitation(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        AdoptionEntity adoption = adoptionService.startTrial(id, null);
        return modelMapper.map(adoption, AdoptionProcessDTO.class);
    }

    @PostMapping("/{id}/return")
    public AdoptionProcessDTO returnPet(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        AdoptionEntity adoption = adoptionService.returnPet(id);
        return modelMapper.map(adoption, AdoptionProcessDTO.class);
    }
}