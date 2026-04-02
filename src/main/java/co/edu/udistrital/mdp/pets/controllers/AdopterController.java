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

import co.edu.udistrital.mdp.pets.dto.AdopterCreationDTO;
import co.edu.udistrital.mdp.pets.dto.AdopterDTO;
import co.edu.udistrital.mdp.pets.dto.AdopterMapper;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdopterService;

@RestController
@RequestMapping("/adopters")
public class AdopterController {

    @Autowired
    private AdopterService adopterService;
    @Autowired
    private AdopterMapper mapper;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdopterCreationDTO> findAll() {
        return adopterService.searchAdopters()
                .stream()
                .map((mapper::entityToDTO))
                .toList();
    }

    @GetMapping(value = "/{adopterId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdopterDTO findOne(@PathVariable Long adopterId) throws EntityNotFoundException {
        return mapper.CdtoTodto(mapper.entityToDTO(adopterService.searchAdopter(adopterId)));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdopterCreationDTO create(@RequestBody AdopterCreationDTO adopterDTO)
            throws IllegalOperationException {
                AdopterEntity adopter = mapper.dtoToEntity(adopterDTO);
                adopterService.createAdopter(adopter);
                return adopterDTO;
    
    }

    @PutMapping(value = "/{adopterId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdopterCreationDTO update(@PathVariable Long adopterId, @RequestBody AdopterCreationDTO adopterDTO)
            throws EntityNotFoundException, IllegalOperationException {
            return mapper.entityToDTO(adopterService.updateAdopter(adopterId, mapper.dtoToEntity(adopterDTO)));
    }

    @DeleteMapping(value = "/{adopterId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long adopterId)
            throws EntityNotFoundException, IllegalOperationException {
        adopterService.deleteAdopter(adopterId);
    }
}
