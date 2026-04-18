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

import co.edu.udistrital.mdp.pets.dto.ProcedureDTO;
import co.edu.udistrital.mdp.pets.dto.ProcedureDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.ProcedureService;

@RestController
@RequestMapping("/procedures")
public class ProcedureController {

    private final ProcedureService procedureService;

    public ProcedureController(ProcedureService procedureService) {
        this.procedureService = procedureService;
    }

    @GetMapping
    public ResponseEntity<List<ProcedureDetailDTO>> getAllProcedures() {
        var entities = procedureService.searchProcedures();
        List<ProcedureDetailDTO> dtos = entities.stream()
                .map(ProcedureDetailDTO::new)
                .toList();
        return new ResponseEntity<>(dtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProcedureDetailDTO> getProcedureById(@PathVariable("id") Long id) throws EntityNotFoundException {
        var entity = procedureService.searchProcedure(id);
        return new ResponseEntity<>(new ProcedureDetailDTO(entity), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProcedureDTO> createProcedure(@RequestBody ProcedureDTO procedureDTO) throws IllegalOperationException {
        var entity = procedureDTO.toEntity();
        var createdEntity = procedureService.createProcedure(entity);
        return new ResponseEntity<>(new ProcedureDTO(createdEntity), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProcedureDTO> updateProcedure(@PathVariable("id") Long id, @RequestBody ProcedureDTO procedureDTO) throws EntityNotFoundException{
        var entity = procedureDTO.toEntity();
        var updatedEntity = procedureService.updateProcedure(id, entity);
        return new ResponseEntity<>(new ProcedureDTO(updatedEntity), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProcedure(@PathVariable("id") Long id) throws EntityNotFoundException {
        procedureService.deleteProcedure(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
