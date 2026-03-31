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

import co.edu.udistrital.mdp.pets.dto.AdopterDTO;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.AdopterService;

@RestController
@RequestMapping("/adopters")
public class AdopterController {

    @Autowired
    private AdopterService adopterService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<AdopterDTO> findAll(@RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String status) {
        List<AdopterEntity> adopters = (name != null || email != null || status != null)
                ? adopterService.searchAdopters(name, email, status)
                : adopterService.searchAdopters();
        return adopters.stream().map(AdopterDTO::new).toList();
    }

    @GetMapping(value = "/{adopterId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdopterDetailDTO findOne(@PathVariable Long adopterId) throws EntityNotFoundException {
        return new AdopterDetailDTO(adopterService.searchAdopter(adopterId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public AdopterDetailDTO create(@RequestBody AdopterDTO adopterDTO)
            throws IllegalOperationException {
        return new AdopterDetailDTO(adopterService.createAdopter(adopterDTO.toEntity()));
    }

    @PutMapping(value = "/{adopterId}")
    @ResponseStatus(code = HttpStatus.OK)
    public AdopterDetailDTO update(@PathVariable Long adopterId, @RequestBody AdopterDTO adopterDTO)
            throws EntityNotFoundException, IllegalOperationException {
        return new AdopterDetailDTO(adopterService.updateAdopter(adopterId, adopterDTO.toEntity()));
    }

    @DeleteMapping(value = "/{adopterId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long adopterId)
            throws EntityNotFoundException, IllegalOperationException {
        adopterService.deleteAdopter(adopterId);
    }
}
