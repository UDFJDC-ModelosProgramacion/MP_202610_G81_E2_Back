package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.MedicationDTO;
import co.edu.udistrital.mdp.pets.dto.MedicationDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.MedicationService;

@RestController
@RequestMapping("/medications")
public class MedicationController {

    private final MedicationService medicationService;

    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public ResponseEntity<List<MedicationDetailDTO>> getAllMedications() {
        var entities = medicationService.searchMedications();
        List<MedicationDetailDTO> dtos = entities.stream()
                .map(MedicationDetailDTO::new)
                .toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicationDetailDTO> getMedicationById(@PathVariable("id") Long id) throws EntityNotFoundException {
        var entity = medicationService.searchMedication(id);
        return new ResponseEntity<>(new MedicationDetailDTO(entity), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MedicationDTO> createMedication(@RequestBody MedicationDTO medicationDTO) throws IllegalOperationException {
        var entity = medicationDTO.toEntity();
        var createdEntity = medicationService.createMedication(entity);
        return new ResponseEntity<>(new MedicationDTO(createdEntity), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicationDTO> updateMedication(@PathVariable("id") Long id, @RequestBody MedicationDTO medicationDTO) throws EntityNotFoundException {
        var entity = medicationDTO.toEntity();
        var updatedEntity = medicationService.updateMedication(id, entity);
        return new ResponseEntity<>(new MedicationDTO(updatedEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable("id") Long id) throws EntityNotFoundException {
        medicationService.deleteMedication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
