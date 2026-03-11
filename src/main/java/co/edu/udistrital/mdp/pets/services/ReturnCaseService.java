package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.ReturnCaseEntity;
import co.edu.udistrital.mdp.pets.repositories.ReturnCaseRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReturnCaseService {

    @Autowired
    private ReturnCaseRepository returnCaseRepository;

    public ReturnCaseEntity createReturnCase(ReturnCaseEntity returnCase){
        log.info("Creating return case");

        if(returnCase == null){
            throw new IllegalArgumentException("Return case cannot be null");
        }

        return returnCaseRepository.save(returnCase);
    }

    @SuppressWarnings("null")
    public ReturnCaseEntity searchReturnCase(Long id){
        log.info("Searching return case with id = {}", id);

        return returnCaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Return case not found"));
    }

    public List<ReturnCaseEntity> searchReturnCases(){
        log.info("Searching all return cases");

        return returnCaseRepository.findAll();
    }

    public ReturnCaseEntity updateReturnCase(Long id, ReturnCaseEntity returnCase){
        log.info("Updating return case with id = {}", id);

        @SuppressWarnings("null")
        ReturnCaseEntity existing = returnCaseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Return case not found"));

        existing.setReturnDate(returnCase.getReturnDate());
        existing.setReason(returnCase.getReason());
        existing.setDetails(returnCase.getDetails());

        return returnCaseRepository.save(existing);
    }

    public void deleteReturnCase(Long id){
        log.info("Deleting return case with id = {}", id);

        @SuppressWarnings("null")
        ReturnCaseEntity returnCase = returnCaseRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Return case not found"));

        if(returnCase.getAdoptionProcess() != null){
            throw new IllegalArgumentException("Cannot delete return case associated to an adoption process");
        }

        returnCaseRepository.delete(returnCase);
    }
}