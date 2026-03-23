package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.*;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRepository;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdoptionService {
    private static final String NOT_FOUND_MSG = "Adoption not found.";
    private static final String ADOPTION_ID_NULL_MSG = "Adoption id cannot be null";
    private static final String AD_NOT_FOUND = NOT_FOUND_MSG;

    @Autowired
    private AdoptionRepository adoptionRepository;

    @Autowired
    private AdopterRepository adopterRepository;

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public AdoptionEntity createAdoption(AdoptionEntity adoption) throws IllegalOperationException {
        log.info("Inicia proceso de creación de la adopción");

        if (adoption == null) {
            throw new IllegalOperationException("Adoption can not be null.");
        }

        // Validar que exista el adoptante
        if (adoption.getAdopter() == null || adoption.getAdopter().getId() == null) {
            throw new IllegalOperationException("Adopter cannot be null.");
        }

        // Validar que exista la mascota
        if (adoption.getPet() == null || adoption.getPet().getId() == null) {
            throw new IllegalOperationException("Pet cannot be null.");
        }

        // Verificar que el adoptante existe en BD
        Optional<AdopterEntity> adopter = adopterRepository.findById(adoption.getAdopter().getId());
        if (adopter.isEmpty()) {
            throw new IllegalOperationException("Adopter does not exist.");
        }

        // Verificar que la mascota existe en BD
        Optional<PetEntity> pet = petRepository.findById(adoption.getPet().getId());
        if (pet.isEmpty()) {
            throw new IllegalOperationException("Pet does not exist.");
        }

        adoption.setAdopter(adopter.get());
        adoption.setPet(pet.get());
        adoption.setStatus(AdoptionStatus.REQUESTED);
        adoption.setContractSigned(false);

        log.info("Termina proceso de creación de la adopción");
        return adoptionRepository.save(adoption);
    }

    @Transactional
    public AdoptionEntity searchAdoption(Long id) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la adopción con id = {}", id);
        Objects.requireNonNull(id, ADOPTION_ID_NULL_MSG);

        Optional<AdoptionEntity> adoptionEntity = adoptionRepository.findById(id);
        if (adoptionEntity.isEmpty())
            throw new EntityNotFoundException(AD_NOT_FOUND);

        log.info("Termina proceso de consultar la adopción con id = {}", id);
        return adoptionEntity.get();
    }

    @Transactional
    public List<AdoptionEntity> searchAdoptions() {
        log.info("Inicia proceso de consultar todas las adopciones");
        return adoptionRepository.findAll();
    }

    @Transactional
    public AdoptionEntity updateAdoption(Long id, AdoptionEntity adoption)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la adopción con id = {}", id);
        Objects.requireNonNull(id, ADOPTION_ID_NULL_MSG);

        Optional<AdoptionEntity> adoptionEntity = adoptionRepository.findById(id);
        if (adoptionEntity.isEmpty())
            throw new EntityNotFoundException(AD_NOT_FOUND);

        AdoptionEntity existing = adoptionEntity.get();
        existing.setOfficialDate(adoption.getOfficialDate());
        existing.setContractSigned(adoption.getContractSigned());

        log.info("Termina proceso de actualizar la adopción con id = {}", id);
        return adoptionRepository.save(existing);
    }

    @Transactional
    public AdoptionEntity requestAdoption(Long id) throws EntityNotFoundException, IllegalOperationException {
        AdoptionEntity adoption = searchAdoption(id);

        if (adoption.getStatus() != AdoptionStatus.REQUESTED) {
            throw new IllegalOperationException("Adoption already requested or in invalid state");
        }

        adoption.setStatus(AdoptionStatus.REQUESTED);
        return adoptionRepository.save(adoption);
    }

    @Transactional
    public AdoptionEntity approveAdoption(Long id) throws EntityNotFoundException, IllegalOperationException {
        AdoptionEntity adoption = searchAdoption(id);

        if (adoption.getStatus() != AdoptionStatus.REQUESTED) {
            throw new IllegalOperationException("Only requested adoptions can be approved");
        }

        adoption.setStatus(AdoptionStatus.APPROVED);
        adoption.setContractSigned(true);
        adoption.setOfficialDate(LocalDate.now());

        return adoptionRepository.save(adoption);
    }

    @Transactional
    public AdoptionEntity returnPet(Long id) throws EntityNotFoundException, IllegalOperationException {
        AdoptionEntity adoption = searchAdoption(id);

        if (adoption.getStatus() != AdoptionStatus.APPROVED &&
            adoption.getStatus() != AdoptionStatus.IN_TRIAL) {
            throw new IllegalOperationException("Adoption not in a returnable state");
        }

        adoption.setStatus(AdoptionStatus.RETURNED);

        return adoptionRepository.save(adoption);
    }

    @Transactional
    public AdoptionEntity startTrial(Long id, TrialCohabitationEntity trial)
            throws EntityNotFoundException, IllegalOperationException {

        AdoptionEntity adoption = searchAdoption(id);

        if (adoption.getStatus() != AdoptionStatus.APPROVED) {
            throw new IllegalOperationException("Trial only allowed after approval");
        }

        adoption.setTrialCohabitation(trial);
        adoption.setStatus(AdoptionStatus.IN_TRIAL);

        return adoptionRepository.save(adoption);
    }

    @Transactional
    public void deleteAdoption(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar la adopción con id = {}", id);
        Objects.requireNonNull(id, ADOPTION_ID_NULL_MSG);

        Optional<AdoptionEntity> adoptionEntity = adoptionRepository.findById(id);
        if (adoptionEntity.isEmpty())
            throw new EntityNotFoundException(AD_NOT_FOUND);

        AdoptionEntity adoption = adoptionEntity.get();

        if (adoption.getTrialCohabitation() != null) {
            throw new IllegalOperationException("Cannot delete adoption with associated trial cohabitation.");
        }

        adoptionRepository.delete(adoption);
        log.info("Termina proceso de borrar la adopción con id = {}", id);
    }
}