package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import co.edu.udistrital.mdp.pets.dto.AdopterDetailDTO;
import co.edu.udistrital.mdp.pets.dto.AdopterDTO;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.mappers.EntityDtoMapper;
import co.edu.udistrital.mdp.pets.services.AdopterService;

import java.util.List;

@RestController
@RequestMapping("/adopters")
public class AdopterController {
    
    @Autowired
    private AdopterService adopterService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AdopterDTO createAdopter(@RequestBody AdopterDTO adopterDto)
            throws IllegalOperationException {
        AdopterEntity saved = adopterService.createAdopter(EntityDtoMapper.toEntity(adopterDto));
        return EntityDtoMapper.toDto(saved);
    }

    @GetMapping
    public List<AdopterDTO> getAdopters() {
        return EntityDtoMapper.toAdopterDtoList(adopterService.searchAdopters());
    }

    @GetMapping("/{id}")
    public AdopterDTO getAdopterById(@PathVariable Long id)
            throws EntityNotFoundException {
        return EntityDtoMapper.toDto(adopterService.searchAdopter(id));
    }

    @GetMapping("/{id}/detail")
    public AdopterDetailDTO getAdopterDetail(@PathVariable Long id)
            throws EntityNotFoundException {
        return EntityDtoMapper.toDetailDto(adopterService.searchAdopter(id));
    }

    @PutMapping("/{id}")
    public AdopterDTO updateAdopter(@PathVariable Long id,
                                    @RequestBody AdopterDTO adopterDto)
            throws EntityNotFoundException, IllegalOperationException {
        AdopterEntity updated = adopterService.updateAdopter(id, EntityDtoMapper.toEntity(adopterDto));
        return EntityDtoMapper.toDto(updated);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAdopter(@PathVariable Long id)
            throws EntityNotFoundException, IllegalOperationException {
        adopterService.deleteAdopter(id);
    }
}