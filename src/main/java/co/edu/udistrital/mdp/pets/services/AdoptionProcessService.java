package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptionProcessRepository;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdoptionProcessService {
    private static final String NOT_FOUND_MSG = "Source not found";

    private static final String ADPROCESS_NOT_FOUND = NOT_FOUND_MSG;

    @Autowired
    private AdoptionProcessRepository adoptionProcessRepository;

    public AdoptionProcessEntity createAdoptionProcess(AdoptionProcessEntity adoptionProcess) throws co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException {
        log.info("Create adoption process");

        if(adoptionProcess == null){
            throw new co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException("Adoption process can't be null");
        }

        if(adoptionProcess.getCreationDate() == null){
            throw new co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException("Creation date can't be null");
        }

        if(adoptionProcess.getAdoptionRequest() == null){
            throw new co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException("Adoption process must belong to an adoption request");
        }

        return adoptionProcessRepository.save(adoptionProcess);
    }

    public AdoptionProcessEntity searchAdoptionProcess(Long id) throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException {
        log.info("Search adoption process with id = {}", id);

        return adoptionProcessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADPROCESS_NOT_FOUND));
    }

    public List<AdoptionProcessEntity> searchAdoptionProcesses(){
        log.info("Search all adoption processes");

        return adoptionProcessRepository.findAll();
    }

    public AdoptionProcessEntity updateAdoptionProcess(Long id, AdoptionProcessEntity adoptionProcess) throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException {
        log.info("Updating adoption process");

        AdoptionProcessEntity existing = adoptionProcessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADPROCESS_NOT_FOUND));

        existing.setCreationDate(adoptionProcess.getCreationDate());
        existing.setStatus(adoptionProcess.getStatus());

        return adoptionProcessRepository.save(existing);
    }

    public void deleteAdoptionProcess(Long id) throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException, co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException {
        log.info("Delete adoption process");

        AdoptionProcessEntity adoptionProcess = adoptionProcessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADPROCESS_NOT_FOUND));

        if(adoptionProcess.getReturnCase() != null){
            throw new co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException("Can't delete adoption process because it has a return case");
        }

        adoptionProcessRepository.delete(adoptionProcess);
    }
}