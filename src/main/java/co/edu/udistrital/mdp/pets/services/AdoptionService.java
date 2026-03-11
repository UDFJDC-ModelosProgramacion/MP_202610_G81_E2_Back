package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class AdoptionService {
    @Autowired
    AdoptionRepository adoptionRepository;

    public AdoptionEntity createAdoption(AdoptionEntity adoption){
        log.info("Creating adoption");

        if(adoption == null){
            throw new IllegalArgumentException("Adoption can not be null.");
        }

        return adoptionRepository.save(adoption);
    }

    @SuppressWarnings("null")
    public AdoptionEntity searchAdoption(Long id){
        log.info("Search adoption with id = {}", id);

        return adoptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Adoption not found."));
    }
    
    public List<AdoptionEntity> searchAdoptions(){
        log.info("Search all adoptions");

        return adoptionRepository.findAll();
    }

    @SuppressWarnings("null")
    public AdoptionEntity updateAdoption(Long id, AdoptionEntity adoption){
        log.info("Updating adoption with id = {}", id);

        AdoptionEntity existing = adoptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Adoption not found."));

        existing.setOfficialDate(adoption.getOfficialDate());
        existing.setContractSigned(adoption.getContractSigned());

        return adoptionRepository.save(existing);
    }

    @SuppressWarnings("null")
    public void deleteAdoption(Long id){
        log.info("Delete adoption with id = {}", id);

        AdoptionEntity adoption = adoptionRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Adoption not found"));
        
        if(adoption.getTrialCohabitation() != null){
            throw new IllegalArgumentException("Cannot delete adoption with associated trial cohabitation.");
        }

        if(adoption.getPet() != null){
            throw new IllegalArgumentException("Cannot delete adoption with associated with a pet.");
        }
        adoptionRepository.delete(adoption);
    }
}
