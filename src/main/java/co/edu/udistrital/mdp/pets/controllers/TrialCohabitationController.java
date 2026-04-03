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

import co.edu.udistrital.mdp.pets.dto.TrialCohabitationDTO;
import co.edu.udistrital.mdp.pets.dto.TrialCohabitationDetailDTO;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.TrialCohabitationService;

@RestController
@RequestMapping("/trial-cohabitations")
public class TrialCohabitationController {

    @Autowired
    private TrialCohabitationService trialCohabitationService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<TrialCohabitationDTO> findAll() {
        return trialCohabitationService.searchTrialCohabitations().stream()
                .map(TrialCohabitationDTO::new).toList();
    }

    @GetMapping(value = "/{trialId}")
    @ResponseStatus(code = HttpStatus.OK)
    public TrialCohabitationDetailDTO findOne(@PathVariable Long trialId)
            throws EntityNotFoundException {
        return new TrialCohabitationDetailDTO(
                trialCohabitationService.searchTrialCohabitation(trialId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public TrialCohabitationDetailDTO create(@RequestBody TrialCohabitationDTO trialDTO)
            throws IllegalOperationException {
        return new TrialCohabitationDetailDTO(
                trialCohabitationService.createTrialCohabitation(trialDTO.toTrialEntity()));
    }

    @PutMapping(value = "/{trialId}")
    @ResponseStatus(code = HttpStatus.OK)
    public TrialCohabitationDetailDTO update(@PathVariable Long trialId,
            @RequestBody TrialCohabitationDTO trialDTO) throws EntityNotFoundException {
        TrialCohabitationEntity updated = trialDTO.toTrialEntity();
        return new TrialCohabitationDetailDTO(
                trialCohabitationService.updateTrialCohabitation(trialId, updated));
    }

    @DeleteMapping(value = "/{trialId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long trialId)
            throws EntityNotFoundException, IllegalOperationException {
        trialCohabitationService.deleteTrialCohabitation(trialId);
    }
}
