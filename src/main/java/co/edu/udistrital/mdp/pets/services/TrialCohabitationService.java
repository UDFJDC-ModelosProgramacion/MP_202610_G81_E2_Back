package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import co.edu.udistrital.mdp.pets.repositories.TrialCohabitationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrialCohabitationService {

    @Autowired
    private TrialCohabitationRepository trialCohabitationRepository;

    public TrialCohabitationEntity createTrialCohabitation(TrialCohabitationEntity trialCohabitation){
        log.info("Create trial cohabitation");

        if(trialCohabitation == null){
            throw new IllegalArgumentException("Trial cohabitation can't be null");
        }

        if(trialCohabitation.getStartDate() == null){
            throw new IllegalArgumentException("Start date can't be null");
        }

        return trialCohabitationRepository.save(trialCohabitation);
    }

    public TrialCohabitationEntity searchTrialCohabitation(Long id){
        log.info("Search trial cohabitation with id = {}", id);

        return trialCohabitationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trial cohabitation not found"));
    }

    public List<TrialCohabitationEntity> searchTrialCohabitations(){
        log.info("Search all trial cohabitations");

        return trialCohabitationRepository.findAll();
    }

    public TrialCohabitationEntity updateTrialCohabitation(Long id, TrialCohabitationEntity trialCohabitation){
        log.info("Updating trial cohabitation");

        TrialCohabitationEntity existing = trialCohabitationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trial cohabitation not found"));

        existing.setStartDate(trialCohabitation.getStartDate());
        existing.setEndDate(trialCohabitation.getEndDate());
        existing.setApproved(trialCohabitation.getApproved());
        existing.setNotes(trialCohabitation.getNotes());

        return trialCohabitationRepository.save(existing);
    }

    public void deleteTrialCohabitation(Long id){
        log.info("Delete trial cohabitation");

        TrialCohabitationEntity trialCohabitation = trialCohabitationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Trial cohabitation not found"));

        trialCohabitationRepository.delete(trialCohabitation);
    }
}