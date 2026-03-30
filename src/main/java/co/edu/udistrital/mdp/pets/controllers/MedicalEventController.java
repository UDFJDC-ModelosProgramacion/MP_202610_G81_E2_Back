package co.edu.udistrital.mdp.pets.controllers;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
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

import co.edu.udistrital.mdp.pets.dto.MedicalEventDTO;
import co.edu.udistrital.mdp.pets.dto.MedicalEventDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.services.MedicalEventService;

@RestController
@RequestMapping("/medical-events")
public class MedicalEventController {

    @Autowired
    private MedicalEventService medicalEventService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<MedicalEventDTO> findAll(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        LocalDate start = startDate != null ? startDate : LocalDate.of(1900, 1, 1);
        LocalDate end = endDate != null ? endDate : LocalDate.now();
        return medicalEventService.searchMedicalEvents(start, end)
                .stream().map(MedicalEventDTO::new).toList();
    }

    @GetMapping(value = "/{eventId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MedicalEventDetailDTO findOne(@PathVariable Long eventId) throws EntityNotFoundException {
        return new MedicalEventDetailDTO(medicalEventService.searchMedicalEvent(eventId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public MedicalEventDetailDTO create(
            @RequestParam Long historyId,
            @RequestParam Long veterinarianId,
            @RequestBody MedicalEventDTO eventDTO) throws EntityNotFoundException {
        return new MedicalEventDetailDTO(
                medicalEventService.createMedicalEvent(historyId, veterinarianId, eventDTO.toEntity()));
    }

    @PutMapping(value = "/{eventId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MedicalEventDetailDTO update(@PathVariable Long eventId,
            @RequestBody MedicalEventDTO eventDTO) throws EntityNotFoundException {
        return new MedicalEventDetailDTO(
                medicalEventService.updateMedicalEvent(eventId, eventDTO.toEntity()));
    }

    @DeleteMapping(value = "/{eventId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long eventId) {
        medicalEventService.deleteMedicalEvent(eventId);
    }
}
