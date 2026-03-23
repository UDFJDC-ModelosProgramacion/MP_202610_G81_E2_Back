package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRequestRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdoptionRequestService {
    private static final String NOT_FOUND_MSG = "Adoption request not found";
        private static final String ADOPTION_REQUEST_ID_NULL_MSG = "Adoption request id cannot be null";

    private static final String ADREQ_NOT_FOUND = NOT_FOUND_MSG;

    @Autowired
    private AdoptionRequestRepository adoptionRequestRepository;

    public AdoptionRequestEntity createAdoptionRequest(AdoptionRequestEntity adoptionRequest) throws co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException {

        log.info("Create adoption request");

        if(adoptionRequest == null){
            throw new co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException("Adoption request can't be null");
        }

        if(adoptionRequest.getRequestDate() == null){
            throw new co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException("Request date can't be null");
        }

        if(adoptionRequest.getPet() == null){
            throw new co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException("Adoption request must have a pet");
        }

        if(adoptionRequest.getAdopter() == null){
            throw new co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException("Adoption request must have an adopter");
        }

        return adoptionRequestRepository.save(adoptionRequest);
    }

    public AdoptionRequestEntity searchAdoptionRequest(Long id) throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException {

        log.info("Search adoption request with id = {}", id);
        Objects.requireNonNull(id, ADOPTION_REQUEST_ID_NULL_MSG);

        return adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADREQ_NOT_FOUND));
    }

    public List<AdoptionRequestEntity> searchAdoptionRequests(){

        log.info("Search all adoption requests");

        return adoptionRequestRepository.findAll();
    }

    public AdoptionRequestEntity updateAdoptionRequest(Long id, AdoptionRequestEntity adoptionRequest) throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException {

        log.info("Updating adoption request");
        Objects.requireNonNull(id, ADOPTION_REQUEST_ID_NULL_MSG);

        AdoptionRequestEntity existing = adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADREQ_NOT_FOUND));

        existing.setRequestDate(adoptionRequest.getRequestDate());
        existing.setStatus(adoptionRequest.getStatus());

        return adoptionRequestRepository.save(existing);
    }

    public void deleteAdoptionRequest(Long id) throws co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException, co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException {

        log.info("Delete adoption request");
        Objects.requireNonNull(id, ADOPTION_REQUEST_ID_NULL_MSG);

        AdoptionRequestEntity adoptionRequest = adoptionRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ADREQ_NOT_FOUND));

        if(adoptionRequest.getAdoptionProcess() != null){
            throw new co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException("Can't delete adoption request because it is associated with an adoption process");
        }

        adoptionRequestRepository.delete(adoptionRequest);
    }
}