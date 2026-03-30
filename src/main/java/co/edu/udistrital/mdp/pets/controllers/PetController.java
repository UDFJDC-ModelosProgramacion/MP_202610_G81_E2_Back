package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.services.PetService;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

import java.util.*;

@RestController
@RequestMapping("/pets")
public class PetController {

    @Autowired
    private PetService petService;

    // Mantenemos tus asociaciones en memoria para no borrarlas
    private Map<Long, List<Map<String, Object>>> medicalHistory = new HashMap<>();
    private Map<Long, List<Map<String, Object>>> vaccines = new HashMap<>();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetEntity createPet(@RequestBody PetEntity pet) 
            throws IllegalOperationException, EntityNotFoundException {
        
        // Tu Service pide shelterId. Lo obtenemos del objeto shelter dentro del JSON
        Long shelterId = pet.getShelter().getId(); 
        return petService.createPet(shelterId, pet);
    }

    @GetMapping
    public List<PetEntity> getPets(
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String temperament) {
        // Usamos el método searchPets con sus filtros
        return petService.searchPets(breed, size, temperament);
    }

    @GetMapping("/{id}")
    public PetEntity getPetById(@PathVariable Long id) throws EntityNotFoundException {
        // Cambiado a getPet(id) para coincidir con tu Service
        return petService.getPet(id);
    }

    @PutMapping("/{id}")
    public PetEntity updatePet(@PathVariable Long id, @RequestBody PetEntity pet) 
            throws EntityNotFoundException {
        return petService.updatePet(id, pet);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePet(@PathVariable Long id) 
            throws EntityNotFoundException, IllegalOperationException {
        petService.deletePet(id);
    }

    // --- Mantenemos tus asociaciones de Historia Clínica y Vacunas ---
    @GetMapping("/{id}/medical-history")
    public List<Map<String, Object>> getMedicalHistory(@PathVariable Long id) {
        return medicalHistory.getOrDefault(id, new ArrayList<>());
    }

    @PostMapping("/{id}/medical-history")
    public List<Map<String, Object>> addMedicalEvent(@PathVariable Long id, @RequestBody Map<String, Object> event) {
        medicalHistory.putIfAbsent(id, new ArrayList<>());
        medicalHistory.get(id).add(event);
        return medicalHistory.get(id);
    }

    @PostMapping("/{id}/vaccines")
    public List<Map<String, Object>> addVaccine(@PathVariable Long id, @RequestBody Map<String, Object> vaccine) {
        vaccines.putIfAbsent(id, new ArrayList<>());
        vaccines.get(id).add(vaccine);
        return vaccines.get(id);
    }

    @GetMapping("/{id}/vaccines")
    public List<Map<String, Object>> getVaccines(@PathVariable Long id) {
        return vaccines.getOrDefault(id, new ArrayList<>());
    }
}