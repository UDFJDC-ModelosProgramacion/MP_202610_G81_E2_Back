package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/shelters")
public class ShelterController {

    private List<Map<String, Object>> shelters = new ArrayList<>();

    private Map<Long, List<Map<String, Object>>> shelterMedia = new HashMap<>();
    private Map<Long, List<Map<String, Object>>> shelterReviews = new HashMap<>();
    private Map<Long, List<Map<String, Object>>> shelterVeterinarians = new HashMap<>();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createShelter(@RequestBody Map<String, Object> shelter) {

        shelter.put("id", shelters.size() + 1);
        shelters.add(shelter);

        return shelter;
    }

    @GetMapping
    public List<Map<String, Object>> getShelters() {
        return shelters;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getShelterById(@PathVariable Long id) {

        return shelters.stream()
                .filter(s -> Long.valueOf(s.get("id").toString()).equals(id))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter not found"));
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateShelter(@PathVariable Long id,
                                             @RequestBody Map<String, Object> updatedShelter) {

        for (Map<String, Object> s : shelters) {
            Long sid = Long.valueOf(s.get("id").toString());

            if (sid.equals(id)) {
                s.putAll(updatedShelter);
                s.put("id", id);
                return s;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter not found");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteShelter(@PathVariable Long id) {

        boolean removed = shelters.removeIf(s ->
                Long.valueOf(s.get("id").toString()).equals(id)
        );

        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter not found");
        }
    }

    @PostMapping("/{id}/media")
    public List<Map<String, Object>> addMedia(@PathVariable Long id,
                                              @RequestBody Map<String, Object> media) {

        media.put("id", new Random().nextInt(1000));

        shelterMedia.putIfAbsent(id, new ArrayList<>());
        shelterMedia.get(id).add(media);

        return shelterMedia.get(id);
    }

    @GetMapping("/{id}/media")
    public List<Map<String, Object>> getMedia(@PathVariable Long id) {
        return shelterMedia.getOrDefault(id, new ArrayList<>());
    }

    @PostMapping("/{id}/reviews")
    public List<Map<String, Object>> addReview(@PathVariable Long id,
                                            @RequestBody Map<String, Object> review) {

        review.put("id", new Random().nextInt(1000));
        review.put("shelterId", id);

        shelterReviews.putIfAbsent(id, new ArrayList<>());
        shelterReviews.get(id).add(review);

        return shelterReviews.get(id);
    }

    @GetMapping("/{id}/reviews")
    public List<Map<String, Object>> getReviews(@PathVariable Long id) {
        return shelterReviews.getOrDefault(id, new ArrayList<>());
    }

    @PostMapping("/{id}/veterinarians")
    public List<Map<String, Object>> addVeterinarian(@PathVariable Long id,
                                                     @RequestBody Map<String, Object> vet) {

        vet.put("id", new Random().nextInt(1000));

        shelterVeterinarians.putIfAbsent(id, new ArrayList<>());
        shelterVeterinarians.get(id).add(vet);

        return shelterVeterinarians.get(id);
    }

    @GetMapping("/{id}/veterinarians")
    public List<Map<String, Object>> getVeterinarians(@PathVariable Long id) {
        return shelterVeterinarians.getOrDefault(id, new ArrayList<>());
    }
}