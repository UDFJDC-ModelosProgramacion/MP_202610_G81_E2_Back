package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdoptionProcessRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdoptionProcessService {
    private static final String NOT_FOUND_MSG = "Adoption process not found";
    private static final String ADOPTION_PROCESS_ID_NULL_MSG = "Adoption process id cannot be null";

    private static final String ADPROCESS_NOT_FOUND = NOT_FOUND_MSG;

    @Autowired
    private AdoptionProcessRepository adoptionProcessRepository;

    public AdoptionProcessEntity createAdoptionProcess(AdoptionProcessEntity adoptionProcess)
            throws IllegalOperationException {
        log.info("Create adoption process");

        if (adoptionProcess == null) {
            throw new IllegalOperationException("Adoption process can't be null");
        }

        if (adoptionProcess.getCreationDate() == null) {
            throw new IllegalOperationException("Creation date can't be null");
        }

        if (adoptionProcess.getAdoptionRequest() == null) {
            throw new IllegalOperationException("Adoption process must belong to an adoption request");
        }

        return adoptionProcessRepository.save(adoptionProcess);
    }

    public AdoptionProcessEntity searchAdoptionProcess(Long id) throws EntityNotFoundException {
        log.info("Search adoption process with id = {}", id);
        Objects.requireNonNull(id, ADOPTION_PROCESS_ID_NULL_MSG);

        return adoptionProcessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADPROCESS_NOT_FOUND));
    }

    public List<AdoptionProcessEntity> searchAdoptionProcesses() {
        log.info("Search all adoption processes");

        return adoptionProcessRepository.findAll();
    }

    public AdoptionProcessEntity updateAdoptionProcess(Long id, AdoptionProcessEntity adoptionProcess)
            throws EntityNotFoundException {
        log.info("Updating adoption process");
        Objects.requireNonNull(id, ADOPTION_PROCESS_ID_NULL_MSG);

        AdoptionProcessEntity existing = adoptionProcessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADPROCESS_NOT_FOUND));

        existing.setCreationDate(adoptionProcess.getCreationDate());
        existing.setStatus(adoptionProcess.getStatus());

        return adoptionProcessRepository.save(existing);
    }

    public void deleteAdoptionProcess(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("Delete adoption process");
        Objects.requireNonNull(id, ADOPTION_PROCESS_ID_NULL_MSG);

        AdoptionProcessEntity adoptionProcess = adoptionProcessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADPROCESS_NOT_FOUND));

        if (adoptionProcess.getReturnCase() != null) {
            throw new IllegalOperationException("Can't delete adoption process because it has a return case");
        }

        adoptionProcessRepository.delete(adoptionProcess);
    }
}