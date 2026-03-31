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

import co.edu.udistrital.mdp.pets.dto.AdoptionDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionService;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {

    @Autowired
    private AdoptionService adoptionService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionDTO> findAll() {
        return adoptionService.searchAdoptions().stream().map(AdoptionDTO::new).toList();
    }

    @GetMapping(value = "/{adoptionId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionDetailDTO findOne(@PathVariable Long adoptionId) throws EntityNotFoundException {
        return new AdoptionDetailDTO(adoptionService.searchAdoption(adoptionId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdoptionDetailDTO create(@RequestBody AdoptionDTO adoptionDTO)
            throws IllegalOperationException {
        return new AdoptionDetailDTO(adoptionService.createAdoption(adoptionDTO.toEntity()));
    }

    @PutMapping(value = "/{adoptionId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionDetailDTO update(@PathVariable Long adoptionId,
            @RequestBody AdoptionDTO adoptionDTO)
            throws EntityNotFoundException, IllegalOperationException {
        return new AdoptionDetailDTO(adoptionService.updateAdoption(adoptionId, adoptionDTO.toEntity()));
    }

    @PutMapping(value = "/{adoptionId}/approve")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionDetailDTO approve(@PathVariable Long adoptionId)
            throws EntityNotFoundException, IllegalOperationException {
        return new AdoptionDetailDTO(adoptionService.approveAdoption(adoptionId));
    }

    @PutMapping(value = "/{adoptionId}/return")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionDetailDTO returnPet(@PathVariable Long adoptionId)
            throws EntityNotFoundException, IllegalOperationException {
        return new AdoptionDetailDTO(adoptionService.returnPet(adoptionId));
    }

    @DeleteMapping(value = "/{adoptionId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long adoptionId)
            throws EntityNotFoundException, IllegalOperationException {
        adoptionService.deleteAdoption(adoptionId);
    }
}