package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import co.edu.udistrital.mdp.pets.dto.PetDetailDTO;
import co.edu.udistrital.mdp.pets.dto.PetDTO;
import co.edu.udistrital.mdp.pets.dto.VaccineDTO;
import co.edu.udistrital.mdp.pets.dto.MedicalHistoryDetailDTO;
import co.edu.udistrital.mdp.pets.services.PetService;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.validation.Valid;

import java.util.*;

@RestController
@RequestMapping("/pets")
public class PetController {

    private final PetService petService;

    public PetController(PetService petService) {
        this.petService = petService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PetDTO createPet(@Valid @RequestBody PetDTO petDto)
            throws IllegalOperationException, EntityNotFoundException {
        var pet = petDto.toEntity();

        return new PetDTO(petService.createPet(petDto.getShelterId(), pet));
    }

    @GetMapping
    public List<PetDetailDTO> getPets(
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) String size,
            @RequestParam(required = false) String temperament) {
        return petService.searchPets(breed, size, temperament).stream().map(PetDetailDTO::new).toList();
    }

    @GetMapping("/{id}")
    public PetDetailDTO getPetById(@PathVariable Long id) throws EntityNotFoundException {
        return new PetDetailDTO(petService.getPet(id));
    }

    /**
     * HU09 – Ver detalle de mascota.
     * Retorna toda la información básica de la mascota, información del refugio,
     * historia clínica completa (vacunas + eventos médicos) desde la base de datos,
     * y los IDs de los procesos de adopción asociados.
     */
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

    /**
     * Retorna la historia clínica completa de una mascota desde la base de datos real,
     * incluyendo eventos médicos y vacunas.
     */
    @GetMapping("/{id}/medical-history")
    public MedicalHistoryDetailDTO getMedicalHistory(@PathVariable Long id) throws EntityNotFoundException {
        var pet = petService.getPet(id);
        if (pet.getMedicalHistory() == null) {
            return new MedicalHistoryDetailDTO();
        }
        return new MedicalHistoryDetailDTO(pet.getMedicalHistory());
    }

    /**
     * Retorna la lista de vacunas de una mascota desde la base de datos real.
     */
    @GetMapping("/{id}/vaccines")
    public List<VaccineDTO> getVaccines(@PathVariable Long id) throws EntityNotFoundException {
        var pet = petService.getPet(id);
        if (pet.getMedicalHistory() == null || pet.getMedicalHistory().getVaccineEntries() == null) {
            return new ArrayList<>();
        }
        return pet.getMedicalHistory().getVaccineEntries().stream()
                .map(VaccineDTO::new)
                .toList();
    }
}