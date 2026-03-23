package co.edu.udistrital.mdp.pets.controllers;


import java.util.*;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/pets")
public class PetController {
    private List<Map<String, Object>> pets = new ArrayList<>();
    private Map<Long, List<Map<String, Object>>> medicalHistory = new HashMap<>();
    private Map<Long, List<Map<String, Object>>> vaccines = new HashMap<>();

    @PostMapping
    public Map<String, Object> createPet(@RequestBody Map<String, Object> pet) {
        pet.put("id", pets.size() + 1);
        pets.add(pet);
        return pet;
    }

    @GetMapping
    public List<Map<String, Object>> getPets() {
        return pets;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getPetById(@PathVariable Long id) {
        for (Map<String, Object> pet : pets){
            Object petId = pet.get("id");

            if(petId != null && Long.valueOf(petId.toString()).equals(id)){
                return pet;
            }
        }
        
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    @DeleteMapping("/{id}")
    public void deletePet(@PathVariable Long id){
        boolean removed = pets.removeIf(pet ->
            Long.valueOf(pet.get("id").toString()).equals(id)
        );

        if(!removed){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
        }
    }

    @PutMapping("/{id}")
    public Map<String, Object> updatePet(@PathVariable Long id, @RequestBody Map<String, Object> updatePet) {
        
        for (Map<String, Object> pet : pets){
            Long petId = Long.valueOf(pet.get("id").toString());

            if(petId.equals(id)){
                pet.putAll(updatePet);
                pet.put("id", id);
                return pet;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Pet not found");
    }
    
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
