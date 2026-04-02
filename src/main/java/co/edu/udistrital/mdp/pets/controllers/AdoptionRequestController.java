package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.AdoptionRequestCreationDTO;
import co.edu.udistrital.mdp.pets.dto.AdoptionRequestMapper;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionRequestService;

@RestController
@RequestMapping("/adoption-requests")
public class AdoptionRequestController {

    @Autowired
    private AdoptionRequestService service;

    @Autowired
    private AdoptionRequestMapper mapper;

    @GetMapping
    public List<AdoptionRequestCreationDTO> findAll() {
        return service.searchAdoptionRequests()
                .stream()
                .map(mapper::entityToDTO)
                .toList();
    }

    @GetMapping("/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionRequestCreationDTO findOne(@PathVariable Long id)
            throws EntityNotFoundException {

        return mapper.entityToDTO(service.searchAdoptionRequest(id));
    }

    @PostMapping
    public AdoptionRequestCreationDTO create(
            @RequestBody AdoptionRequestCreationDTO dto)
            throws IllegalOperationException {

        service.createAdoptionRequest(mapper.dtoToEntity(dto));
        return dto;
    }

    @DeleteMapping(value="/{AdoptionRequestId")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws EntityNotFoundException, IllegalOperationException{
        service.deleteAdoptionRequest(id);
    }

}
