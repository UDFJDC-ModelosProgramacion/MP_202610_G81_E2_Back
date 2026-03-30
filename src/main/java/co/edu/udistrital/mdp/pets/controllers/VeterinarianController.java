package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/veterinarians")
public class VeterinarianController {

    private List<Map<String, Object>> veterinarians = new ArrayList<>();

    @PostMapping
    public Map<String, Object> createVeterinarian(@RequestBody Map<String, Object> vet) {
        vet.put("id", veterinarians.size() + 1);
        veterinarians.add(vet);
        return vet;
    }

    @GetMapping
    public List<Map<String, Object>> getVeterinarians() {
        return veterinarians;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getVeterinarianById(@PathVariable Long id) {
        for (Map<String, Object> vet : veterinarians) {
            Object vetId = vet.get("id");

            if (vetId != null && Long.valueOf(vetId.toString()).equals(id)) {
                return vet;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterinarian not found");
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateVeterinarian(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updatedVet) {

        for (Map<String, Object> vet : veterinarians) {
            Long vetId = Long.valueOf(vet.get("id").toString());

            if (vetId.equals(id)) {
                vet.putAll(updatedVet);
                vet.put("id", id);
                return vet;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterinarian not found");
    }

    @DeleteMapping("/{id}")
    public void deleteVeterinarian(@PathVariable Long id) {
        boolean removed = veterinarians.removeIf(vet ->
                Long.valueOf(vet.get("id").toString()).equals(id)
        );

        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Veterinarian not found");
        }
    }
}