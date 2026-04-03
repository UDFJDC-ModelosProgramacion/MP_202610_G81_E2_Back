package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.AdoptionProcessDTO;
import co.edu.udistrital.mdp.pets.dto.AdoptionProcessDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionProcessService;

@RestController
@RequestMapping("/adoption-processes")
public class AdoptionProcessController {

    private final AdoptionProcessService adoptionProcessService;

    public AdoptionProcessController(AdoptionProcessService adoptionProcessService) {
        this.adoptionProcessService = adoptionProcessService;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdoptionProcessDTO> findAll() {
        return adoptionProcessService.searchAdoptionProcesses().stream()
                .map(AdoptionProcessDTO::new).toList();
    }

    @GetMapping(value = "/{processId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionProcessDetailDTO findOne(@PathVariable Long processId)
            throws EntityNotFoundException {
        return new AdoptionProcessDetailDTO(adoptionProcessService.searchAdoptionProcess(processId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdoptionProcessDetailDTO create(@RequestBody AdoptionProcessDTO processDTO)
            throws IllegalOperationException {
        return new AdoptionProcessDetailDTO(
                adoptionProcessService.createAdoptionProcess(processDTO.toEntity()));
    }

    @PutMapping(value = "/{processId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdoptionProcessDetailDTO update(@PathVariable Long processId,
            @RequestBody AdoptionProcessDTO processDTO) throws EntityNotFoundException {
        return new AdoptionProcessDetailDTO(
                adoptionProcessService.updateAdoptionProcess(processId, processDTO.toEntity()));
    }

    @DeleteMapping(value = "/{processId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long processId)
            throws EntityNotFoundException, IllegalOperationException {
        adoptionProcessService.deleteAdoptionProcess(processId);
    }
}
