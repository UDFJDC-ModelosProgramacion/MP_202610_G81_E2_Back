package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.ReturnCaseDTO;
import co.edu.udistrital.mdp.pets.dto.ReturnCaseDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.ReturnCaseService;

@RestController
@RequestMapping("/return-cases")
public class ReturnCaseController {

    @Autowired
    private ReturnCaseService returnCaseService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ReturnCaseDTO> findAll() {
        return returnCaseService.searchReturnCases().stream()
                .map(ReturnCaseDTO::new).toList();
    }

    @GetMapping(value = "/{returnCaseId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ReturnCaseDetailDTO findOne(@PathVariable Long returnCaseId)
            throws EntityNotFoundException {
        return new ReturnCaseDetailDTO(returnCaseService.searchReturnCase(returnCaseId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ReturnCaseDetailDTO create(@RequestBody ReturnCaseDTO returnCaseDTO)
            throws IllegalOperationException {
        return new ReturnCaseDetailDTO(returnCaseService.createReturnCase(returnCaseDTO.toEntity()));
    }

    @PutMapping(value = "/{returnCaseId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ReturnCaseDetailDTO update(@PathVariable Long returnCaseId,
            @RequestBody ReturnCaseDTO returnCaseDTO)
            throws EntityNotFoundException, IllegalOperationException {
        return new ReturnCaseDetailDTO(
                returnCaseService.updateReturnCase(returnCaseId, returnCaseDTO.toEntity()));
    }

    @DeleteMapping(value = "/{returnCaseId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long returnCaseId)
            throws EntityNotFoundException, IllegalOperationException {
        returnCaseService.deleteReturnCase(returnCaseId);
    }
}
