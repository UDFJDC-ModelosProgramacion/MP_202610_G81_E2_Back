package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.TrialCohabitationRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TrialCohabitationService {
    private static final String NOT_FOUND_MSG = "Trial cohabitation not found";
    private static final String TRIAL_COHABITATION_ID_NULL_MSG = "Trial cohabitation id cannot be null";

    private static final String TRIAL_NOT_FOUND = NOT_FOUND_MSG;

    @Autowired
    private TrialCohabitationRepository trialCohabitationRepository;

    public TrialCohabitationEntity createTrialCohabitation(TrialCohabitationEntity trialCohabitation) throws co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException {
        log.info("Create trial cohabitation");

        if(trialCohabitation == null){
            throw new co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException("Trial cohabitation can't be null");
        }

        if(trialCohabitation.getStartDate() == null){
            throw new co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException("Start date can't be null");
        }

        return trialCohabitationRepository.save(trialCohabitation);
    }

    public TrialCohabitationEntity searchTrialCohabitation(Long id) throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException {
        log.info("Search trial cohabitation with id = {}", id);
        Objects.requireNonNull(id, TRIAL_COHABITATION_ID_NULL_MSG);

        return trialCohabitationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TRIAL_NOT_FOUND));
    }

    public List<TrialCohabitationEntity> searchTrialCohabitations(){
        log.info("Search all trial cohabitations");

        return trialCohabitationRepository.findAll();
    }

    public TrialCohabitationEntity updateTrialCohabitation(Long id, TrialCohabitationEntity trialCohabitation) throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException {
        log.info("Updating trial cohabitation");
        Objects.requireNonNull(id, TRIAL_COHABITATION_ID_NULL_MSG);

        TrialCohabitationEntity existing = trialCohabitationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TRIAL_NOT_FOUND));

        existing.setStartDate(trialCohabitation.getStartDate());
        existing.setEndDate(trialCohabitation.getEndDate());
        existing.setApproved(trialCohabitation.getApproved());
        existing.setNotes(trialCohabitation.getNotes());

        return trialCohabitationRepository.save(existing);
    }

    public void deleteTrialCohabitation(Long id) throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException, co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException {
        log.info("Delete trial cohabitation");
        Objects.requireNonNull(id, TRIAL_COHABITATION_ID_NULL_MSG);

        TrialCohabitationEntity trialCohabitation = trialCohabitationRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(TRIAL_NOT_FOUND));

        TrialCohabitationEntity safeTrialCohabitation = Objects.requireNonNull(trialCohabitation,
            "Trial cohabitation cannot be null");
        trialCohabitationRepository.delete(safeTrialCohabitation);
    }
}