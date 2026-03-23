package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/adoptions")
public class AdoptionController {
    private List<Map<String, Object>> adoptions = new ArrayList<>();

    @PostMapping
    public Map<String, Object> createAdoption(@RequestBody Map<String, Object> adoption) {
        adoption.put("id", adoptions.size() + 1);

        adoption.putIfAbsent("status", "REQUESTED");
        adoption.putIfAbsent("contractSigned", false);

        adoptions.add(adoption);
        return adoption;
    }

    @GetMapping
    public List<Map<String, Object>> getAdoptions() {
        return adoptions;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getAdoptionById(@PathVariable Long id) {
        return findAdoptionById(id);
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateAdoption(@PathVariable Long id,
                                            @RequestBody Map<String, Object> updatedAdoption) {

        Map<String, Object> adoption = findAdoptionById(id);

        adoption.putAll(updatedAdoption);
        adoption.put("id", id);

        return adoption;
    }

    @DeleteMapping("/{id}")
    public void deleteAdoption(@PathVariable Long id) {
        boolean removed = adoptions.removeIf(adoption ->
                Long.valueOf(adoption.get("id").toString()).equals(id)
        );

        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Adoption not found");
        }
    }

    @PostMapping("/{id}/request")
    public Map<String, Object> requestAdoption(@PathVariable Long id) {
        Map<String, Object> adoption = findAdoptionById(id);
        adoption.put("status", "REQUESTED");
        return adoption;
    }

    @PostMapping("/{id}/approve")
    public Map<String, Object> approveAdoption(@PathVariable Long id) {
        Map<String, Object> adoption = findAdoptionById(id);
        adoption.put("status", "APPROVED");
        return adoption;
    }

    @PostMapping("/{id}/trial")
    public Map<String, Object> trialCohabitation(@PathVariable Long id) {
        Map<String, Object> adoption = findAdoptionById(id);
        adoption.put("status", "TRIAL");
        return adoption;
    }

    @PostMapping("/{id}/return")
    public Map<String, Object> returnPet(@PathVariable Long id) {
        Map<String, Object> adoption = findAdoptionById(id);
        adoption.put("status", "RETURNED");
        return adoption;
    }

    private Map<String, Object> findAdoptionById(Long id) {
        for (Map<String, Object> adoption : adoptions) {
            Long adoptionId = Long.valueOf(adoption.get("id").toString());

            if (adoptionId.equals(id)) {
                return adoption;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Adoption not found");
    }
}
