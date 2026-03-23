package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdopterService;

import java.util.List;

@RestController
@RequestMapping("/adopters")
public class AdopterController {
    
    @Autowired
    private AdopterService adopterService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdopterEntity createAdopter(@RequestBody AdopterEntity adopter)
            throws IllegalOperationException {
        return adopterService.createAdopter(adopter);
    }

    @GetMapping
    public List<AdopterEntity> getAdopters() {
        return adopterService.searchAdopters();
    }

    @GetMapping("/{id}")
    public AdopterEntity getAdopterById(@PathVariable Long id)
            throws EntityNotFoundException {
        return adopterService.searchAdopter(id);
    }

    @PutMapping("/{id}")
    public AdopterEntity updateAdopter(@PathVariable Long id,
                                    @RequestBody AdopterEntity adopter)
            throws EntityNotFoundException, IllegalOperationException {
        return adopterService.updateAdopter(id, adopter);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdopter(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        adopterService.deleteAdopter(id);
    }
}