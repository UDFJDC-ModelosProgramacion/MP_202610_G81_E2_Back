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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.AdoptionMapper;
import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.dto.AdoptionCreationDTO;
import co.edu.udistrital.mdp.pets.dto.AdoptionDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionService;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;
    @Autowired
    private AdoptionMapper mapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionCreationDTO> findAll() {
               return adoptionService.searchAdoptions()
                .stream()
                .map((mapper::entityToDTO))
                .toList();
    }

    @GetMapping(value = "/{adoptionId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionDTO findOne(@PathVariable Long adoptionId) throws EntityNotFoundException {
        return mapper.CdtoTodto(mapper.entityToDTO(adoptionService.searchAdoption(adoptionId)));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdoptionCreationDTO create(@RequestBody AdoptionCreationDTO dto)
            throws IllegalOperationException {
                     AdoptionEntity adoption = mapper.dtoToEntity(dto);
                adoptionService.createAdoption(adoption);
                return dto;
    }

    @PutMapping(value = "/{adoptionId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionCreationDTO update(@PathVariable Long adoptionId,
            @RequestBody AdoptionCreationDTO adoptionDTO)
            throws EntityNotFoundException, IllegalOperationException {
            return mapper.entityToDTO(adoptionService.updateAdoption(adoptionId, mapper.dtoToEntity(adoptionDTO)));
    }

    @PutMapping(value = "/{adoptionId}/approve")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionCreationDTO approve(@PathVariable Long adoptionId)
            throws EntityNotFoundException, IllegalOperationException {
            return mapper.entityToDTO(adoptionService.approveAdoption(adoptionId));
    }

    @PutMapping(value = "/{adoptionId}/return")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionCreationDTO returnPet(@PathVariable Long adoptionId)
            throws EntityNotFoundException, IllegalOperationException {
            return mapper.entityToDTO(adoptionService.returnPet(adoptionId));
    }

    @DeleteMapping(value = "/{adoptionId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long adoptionId)
            throws EntityNotFoundException, IllegalOperationException {
        adoptionService.deleteAdoption(adoptionId);
    }
}