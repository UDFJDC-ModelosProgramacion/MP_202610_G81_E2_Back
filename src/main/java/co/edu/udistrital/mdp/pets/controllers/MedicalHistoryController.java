package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.MedicalHistoryDTO;
import co.edu.udistrital.mdp.pets.dto.MedicalHistoryDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.MedicalHistoryService;

@RestController
@RequestMapping("/medical-histories")
public class MedicalHistoryController {

    @Autowired
    private MedicalHistoryService medicalHistoryService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<MedicalHistoryDTO> findAll() {
        return medicalHistoryService.searchMedicalHistories()
                .stream().map(MedicalHistoryDTO::new).toList();
    }

    @GetMapping(value = "/pet/{petId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MedicalHistoryDetailDTO findByPet(@PathVariable Long petId) throws EntityNotFoundException {
        return new MedicalHistoryDetailDTO(medicalHistoryService.searchMedicalHistory(petId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public MedicalHistoryDetailDTO create(@RequestParam Long petId,
            @RequestParam Long veterinarianId) throws EntityNotFoundException {
        return new MedicalHistoryDetailDTO(
                medicalHistoryService.createMedicalHistory(petId, veterinarianId));
    }

    @PutMapping(value = "/{historyId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MedicalHistoryDetailDTO update(@PathVariable Long historyId) throws EntityNotFoundException {
        return new MedicalHistoryDetailDTO(medicalHistoryService.updateMedicalHistory(historyId));
    }

    @DeleteMapping(value = "/{historyId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long historyId)
            throws EntityNotFoundException, IllegalOperationException {
        medicalHistoryService.deleteMedicalHistory(historyId);
    }
}
