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

import co.edu.udistrital.mdp.pets.dto.AdoptionRequestDTO;
import co.edu.udistrital.mdp.pets.dto.AdoptionRequestDetailDTO;
import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionRequestService;

@RestController
@RequestMapping("/adoption-requests")
public class AdoptionRequestController {

    @Autowired
    private AdoptionRequestService adoptionRequestService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionRequestDTO> findAll() {
        return adoptionRequestService.searchAdoptionRequests()
                .stream().map(AdoptionRequestDTO::new).toList();
    }

    @GetMapping(value = "/{requestId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionRequestDetailDTO findOne(@PathVariable Long requestId)
            throws EntityNotFoundException {
        return new AdoptionRequestDetailDTO(adoptionRequestService.searchAdoptionRequest(requestId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdoptionRequestDetailDTO create(@RequestBody AdoptionRequestDTO requestDTO)
            throws IllegalOperationException {
        AdoptionRequestEntity entity = requestDTO.toEntity();
        if (requestDTO.getAdopterId() != null) {
            co.edu.udistrital.mdp.pets.entities.AdopterEntity adopter =
                    new co.edu.udistrital.mdp.pets.entities.AdopterEntity();
            adopter.setId(requestDTO.getAdopterId());
            entity.setAdopter(adopter);
        }
        if (requestDTO.getPetId() != null) {
            co.edu.udistrital.mdp.pets.entities.PetEntity pet =
                    new co.edu.udistrital.mdp.pets.entities.PetEntity();
            pet.setId(requestDTO.getPetId());
            entity.setPet(pet);
        }
        return new AdoptionRequestDetailDTO(adoptionRequestService.createAdoptionRequest(entity));
    }

    @PutMapping(value = "/{requestId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionRequestDetailDTO update(@PathVariable Long requestId,
            @RequestBody AdoptionRequestDTO requestDTO)
            throws EntityNotFoundException {
        return new AdoptionRequestDetailDTO(
                adoptionRequestService.updateAdoptionRequest(requestId, requestDTO.toEntity()));
    }

    @DeleteMapping(value = "/{requestId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long requestId)
            throws EntityNotFoundException, IllegalOperationException {
        adoptionRequestService.deleteAdoptionRequest(requestId);
    }
}
