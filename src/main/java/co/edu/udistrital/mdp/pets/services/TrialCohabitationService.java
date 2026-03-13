package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import jakarta.persistence.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.TrialCohabitationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrialCohabitationService {
    @Autowired
    private TrialCohabitationRepository trialCohabitationRepository;

    public TrialCohabitationEntity createTrialCohabitation( TrialCohabitationEntity trialCohabitation){
        log.info("create trialcohabitation");
        if(trialCohabitation ==  null ){
            throw new IllegalArgumentException(" Trial cohabitation can´t be null");
        }
        if(trialCohabitation.getStartDate() == null){
            throw new IllegalArgumentException("Start Date can´t be null");
        }
        if(trialCohabitation.getAdoptionProcess() == null ){
        
            throw new IllegalArgumentException(" Trial cohabitation must belong to an adoption process");

        }
        return trialCohabitationRepository.save(trialCohabitation);
    }
    public TrialCohabitationEntity searchTrialCohabitation(Long id){
        log.info("Search trial cohabitation with id = {}", id);

        return trialCohabitationRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Trial cohabitation not found"));
    }
    public List<TrialCohabitationEntity> searchTrialcohabitations(){
        log.info("Search all trial cohabitation");

        return trialCohabitationRepository.findAll();
    }
    public TrialCohabitationEntity updateTrialCohabitatio(Long id, TrialCohabitationEntity trialCohabitation){
        log.info("updating trial cohabiatation");
        TrialCohabitationEntity existing = trialCohabitationRepository.findById(id).orElseThrow(()->new EntityNotFoundException("Trial cohabitation not found"));

        if(!existing.getStatus().equals("ACTIVE")){
            throw new IllegalArgumentException("Trial cohabitation is not active");
        }
        existing.setStartDate(trialCohabitation.getStartDate());
        existing.setEndDate(trialCohabitation.getEndDate());
        existing.setNotes(trialCohabitation.getNotes());
        existing.setApproved(trialCohabitation.getApproved());

        return trialCohabitationRepository.save(existing);

    }
        
}
