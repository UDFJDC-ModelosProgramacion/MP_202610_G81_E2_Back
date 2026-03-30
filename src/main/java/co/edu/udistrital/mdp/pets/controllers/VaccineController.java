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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.VaccineDTO;
import co.edu.udistrital.mdp.pets.dto.VaccineDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.services.VaccineEntryService;

@RestController
@RequestMapping("/vaccines")
public class VaccineController {

    @Autowired
    private VaccineEntryService vaccineEntryService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<VaccineDTO> findAll(@RequestParam Long medicalHistoryId) {
        return vaccineEntryService.getVaccineEntries(medicalHistoryId)
                .stream().map(VaccineDTO::new).toList();
    }

    @GetMapping(value = "/due")
    @ResponseStatus(code = HttpStatus.OK)
    public List<VaccineDTO> findDue() {
        return vaccineEntryService.findVaccinesDue()
                .stream().map(VaccineDTO::new).toList();
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public VaccineDetailDTO create(@RequestParam Long medicalHistoryId,
            @RequestBody VaccineDTO vaccineDTO) throws EntityNotFoundException {
        return new VaccineDetailDTO(
                vaccineEntryService.createVaccineEntry(medicalHistoryId, vaccineDTO.toEntity()));
    }

    @PutMapping(value = "/{vaccineId}")
    @ResponseStatus(code = HttpStatus.OK)
    public VaccineDetailDTO update(@PathVariable Long vaccineId,
            @RequestBody VaccineDTO vaccineDTO) throws EntityNotFoundException {
        return new VaccineDetailDTO(
                vaccineEntryService.updateVaccineEntry(vaccineId, vaccineDTO.toEntity()));
    }
}
