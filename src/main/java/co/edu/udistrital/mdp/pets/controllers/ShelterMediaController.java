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

import co.edu.udistrital.mdp.pets.dto.ShelterMediaDTO;
import co.edu.udistrital.mdp.pets.dto.ShelterMediaDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.ShelterMediaService;

@RestController
@RequestMapping("/shelter-media")
public class ShelterMediaController {

    private final ShelterMediaService shelterMediaService;

    public ShelterMediaController(ShelterMediaService shelterMediaService) {
        this.shelterMediaService = shelterMediaService;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ShelterMediaDTO> findByShelter(@RequestParam Long shelterId) {
        return shelterMediaService.searchShelterMediasByShelterId(shelterId)
                .stream().map(ShelterMediaDTO::new).toList();
    }

    @GetMapping(value = "/{mediaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterMediaDetailDTO findOne(@PathVariable Long mediaId) throws EntityNotFoundException {
        return new ShelterMediaDetailDTO(shelterMediaService.searchShelterMedia(mediaId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ShelterMediaDetailDTO create(@RequestBody ShelterMediaDTO mediaDTO,
            @RequestParam(defaultValue = "0") long fileSizeBytes)
            throws IllegalOperationException {
        return new ShelterMediaDetailDTO(
                shelterMediaService.createShelterMedia(mediaDTO.toEntity(), fileSizeBytes));
    }

    @PutMapping(value = "/{mediaId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ShelterMediaDetailDTO update(@PathVariable Long mediaId,
            @RequestBody ShelterMediaDTO mediaDTO) throws EntityNotFoundException {
        return new ShelterMediaDetailDTO(
                shelterMediaService.updateShelterMedia(mediaId, mediaDTO.toEntity()));
    }

    @DeleteMapping(value = "/{mediaId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long mediaId)
            throws EntityNotFoundException, IllegalOperationException {
        shelterMediaService.deleteShelterMedia(mediaId);
    }
}
