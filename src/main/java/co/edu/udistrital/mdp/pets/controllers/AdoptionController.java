package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdoptionService;

import java.util.List;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {
    
    @Autowired
    private AdoptionService adoptionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdoptionEntity createAdoption(@RequestBody AdoptionEntity adoption)
            throws IllegalOperationException {
        return adoptionService.createAdoption(adoption);
    }

    @GetMapping
    public List<AdoptionEntity> getAdoptions() {
        return adoptionService.searchAdoptions();
    }

    @GetMapping("/{id}")
    public AdoptionEntity getAdoptionById(@PathVariable Long id)
            throws EntityNotFoundException {
        return adoptionService.searchAdoption(id);
    }

    @PutMapping("/{id}")
    public AdoptionEntity updateAdoption(@PathVariable Long id,
                                        @RequestBody AdoptionEntity adoption)
            throws EntityNotFoundException, IllegalOperationException {
        return adoptionService.updateAdoption(id, adoption);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdoption(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        adoptionService.deleteAdoption(id);
    }

    @PostMapping("/{id}/request")
    public AdoptionEntity requestAdoption(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        return adoptionService.requestAdoption(id);
    }

    @PostMapping("/{id}/approve")
    public AdoptionEntity approveAdoption(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        return adoptionService.approveAdoption(id);
    }

    @PostMapping("/{id}/trial")
    public AdoptionEntity trialCohabitation(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        return adoptionService.startTrial(id, null);
    }

    @PostMapping("/{id}/return")
    public AdoptionEntity returnPet(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        return adoptionService.returnPet(id);
    }
}