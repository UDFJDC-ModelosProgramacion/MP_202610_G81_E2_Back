package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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

import co.edu.udistrital.mdp.pets.dto.InfectionDTO;
import co.edu.udistrital.mdp.pets.dto.InfectionDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.InfectionService;

@RestController
@RequestMapping("/infections")
public class InfectionController {

    @Autowired
    private InfectionService infectionService;

    @GetMapping
    public ResponseEntity<List<InfectionDetailDTO>> getAllInfections() {
        var entities = infectionService.searchInfections();
        var dtos = entities.stream()
                .map(InfectionDetailDTO::new).toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InfectionDetailDTO> getInfectionById(@PathVariable("id") Long id) throws EntityNotFoundException {
        var entity = infectionService.searchInfection(id);
        return new ResponseEntity<>(new InfectionDetailDTO(entity), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<InfectionDTO> createInfection(@RequestBody InfectionDTO infectionDTO) throws IllegalOperationException {
        var entity = infectionDTO.toEntity();
        var createdEntity = infectionService.createInfection(entity);
        return new ResponseEntity<>(new InfectionDTO(createdEntity), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InfectionDTO> updateInfection(@PathVariable("id") Long id, @RequestBody InfectionDTO infectionDTO) throws EntityNotFoundException{
        var entity = infectionDTO.toEntity();
        var updatedEntity = infectionService.updateInfection(id, entity);
        return new ResponseEntity<>(new InfectionDTO(updatedEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInfection(@PathVariable("id") Long id) throws EntityNotFoundException {
        infectionService.deleteInfection(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
