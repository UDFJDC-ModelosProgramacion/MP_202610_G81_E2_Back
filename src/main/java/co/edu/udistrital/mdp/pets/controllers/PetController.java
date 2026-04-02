package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.pets.dto.PetDetailDTO;
import co.edu.udistrital.mdp.pets.dto.PetDTO;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
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
    public PetDTO createPet(@RequestBody PetDTO petDto)
            throws IllegalOperationException, EntityNotFoundException {
        if (petDto.getShelterId() == null) {
            throw new IllegalOperationException("shelterId is required");
        }
        PetEntity pet = petDto.toEntity();
        ShelterEntity shelter = new ShelterEntity();
        shelter.setId(petDto.getShelterId());
        pet.setShelter(shelter);

        return new PetDTO(petService.createPet(petDto.getShelterId(), pet));
    }

    @GetMapping
    public List<PetDTO> getPets(
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String temperament) {
        return petService.searchPets(breed, size, temperament).stream().map(PetDTO::new).toList();
    }

    @GetMapping("/{id}")
    public PetDTO getPetById(@PathVariable Long id) throws EntityNotFoundException {
        return new PetDTO(petService.getPet(id));
    }

    @GetMapping("/{id}/detail")
    public PetDetailDTO getPetDetail(@PathVariable Long id) throws EntityNotFoundException {
        return new PetDetailDTO(petService.getPet(id));
    }

    @PutMapping("/{id}")
    public PetDTO updatePet(@PathVariable Long id, @RequestBody PetDTO petDto)
            throws EntityNotFoundException {
        return new PetDTO(petService.updatePet(id, petDto.toEntity()));
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