package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

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

import co.edu.udistrital.mdp.pets.dto.SpecialtiesDTO;
import co.edu.udistrital.mdp.pets.dto.SpecialtiesDetailDTO;
import co.edu.udistrital.mdp.pets.entities.VetSpecialityEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.SpecialityService;

@RestController
@RequestMapping("/specialties")
public class SpecialtiesController {

    private final SpecialityService specialityService;

    public SpecialtiesController(SpecialityService specialityService) {
        this.specialityService = specialityService;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<SpecialtiesDTO> findAll(@RequestParam(required = false) String name) {
        List<VetSpecialityEntity> specialities = specialityService.searchSpecialities(name);
        return specialities.stream().map(SpecialtiesDTO::new).toList();
    }

    @GetMapping(value = "/{specialtyId}")
    @ResponseStatus(code = HttpStatus.OK)
    public SpecialtiesDetailDTO findOne(@PathVariable Long specialtyId) throws EntityNotFoundException {
        return new SpecialtiesDetailDTO(specialityService.searchSpeciality(specialtyId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public SpecialtiesDetailDTO create(@RequestBody SpecialtiesDTO specialtyDTO)
            throws IllegalOperationException {
        return new SpecialtiesDetailDTO(specialityService.createSpeciality(specialtyDTO.toEntity()));
    }

    @PutMapping(value = "/{specialtyId}")
    @ResponseStatus(code = HttpStatus.OK)
    public SpecialtiesDetailDTO update(@PathVariable Long specialtyId,
            @RequestBody SpecialtiesDTO specialtyDTO)
            throws EntityNotFoundException, IllegalOperationException {
        return new SpecialtiesDetailDTO(
                specialityService.updateSpeciality(specialtyId, specialtyDTO.toEntity()));
    }

    @DeleteMapping(value = "/{specialtyId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long specialtyId)
            throws EntityNotFoundException, IllegalOperationException {
        specialityService.deleteSpeciality(specialtyId);
    }
}
