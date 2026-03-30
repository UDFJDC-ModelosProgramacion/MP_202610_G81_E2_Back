package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;
import co.edu.udistrital.mdp.pets.services.ReviewService;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

import java.util.*;

@RestController
@RequestMapping("/shelters")
public class ShelterController {

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private ReviewService reviewService;

    // Estructuras en memoria para lo que aún no tiene Entidad/Service persistente
    private Map<Long, List<Map<String, Object>>> shelterMedia = new HashMap<>();
    private Map<Long, List<Map<String, Object>>> shelterVeterinarians = new HashMap<>();

    // --- Métodos Base del Shelter ---

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ShelterEntity createShelter(@RequestBody ShelterEntity shelter) {
        return shelterRepository.save(shelter);
    }

    @GetMapping
    public List<ShelterEntity> getShelters() {
        return shelterRepository.findAll();
    }

    @GetMapping("/{id}")
    public ShelterEntity getShelterById(@PathVariable Long id) {
        return shelterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter not found"));
    }

    // --- Métodos de REVIEWS (Conectados a ReviewService) ---

    @PostMapping("/{id}/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ReviewEntity addReview(@PathVariable Long id, @RequestBody ReviewEntity review) 
            throws IllegalOperationException {
        
        // Buscamos el shelter real en la DB
        ShelterEntity shelter = shelterRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter not found"));

        // Vinculamos y guardamos mediante el servicio persistente
        review.setShelter(shelter);
        return reviewService.createReview(review);
    }

    @GetMapping("/{id}/reviews")
    public List<ReviewEntity> getReviews(@PathVariable Long id) {
        // Por ahora devuelve todas las de la DB. 
        // Tip: Podrías filtrar en el Service por shelterId después.
        return reviewService.searchReviews();
    }

    // --- Métodos de VETERINARIANS (Memoria - Map) ---

    @PostMapping("/{id}/veterinarians")
    public List<Map<String, Object>> addVeterinarian(@PathVariable Long id, @RequestBody Map<String, Object> vet) {
        // Validamos que el shelter exista en la DB antes de agregar al mapa
        if (!shelterRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter not found");
        }

        vet.put("id", new Random().nextInt(1000));
        shelterVeterinarians.putIfAbsent(id, new ArrayList<>());
        shelterVeterinarians.get(id).add(vet);
        return shelterVeterinarians.get(id);
    }

    @GetMapping("/{id}/veterinarians")
    public List<Map<String, Object>> getVeterinarians(@PathVariable Long id) {
        return shelterVeterinarians.getOrDefault(id, new ArrayList<>());
    }

    // --- Métodos de MEDIA (Memoria - Map) ---

    @PostMapping("/{id}/media")
    public List<Map<String, Object>> addMedia(@PathVariable Long id, @RequestBody Map<String, Object> media) {
        if (!shelterRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter not found");
        }

        media.put("id", new Random().nextInt(1000));
        shelterMedia.putIfAbsent(id, new ArrayList<>());
        shelterMedia.get(id).add(media);
        return shelterMedia.get(id);
    }

    @GetMapping("/{id}/media")
    public List<Map<String, Object>> getMedia(@PathVariable Long id) {
        return shelterMedia.getOrDefault(id, new ArrayList<>());
    }
}