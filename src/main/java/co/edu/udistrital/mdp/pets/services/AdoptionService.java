package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    private final AdoptionRepository adoptionRepository;
    private final AdopterRepository adopterRepository;
    private final PetRepository petRepository;

    public AdoptionService(
            AdoptionRepository adoptionRepository,
            AdopterRepository adopterRepository,
            PetRepository petRepository) {
        this.adoptionRepository = adoptionRepository;
        this.adopterRepository = adopterRepository;
        this.petRepository = petRepository;
    }

    @Transactional
    public AdoptionEntity createAdoption(AdoptionEntity adoption) throws IllegalOperationException {
        log.info("Inicia proceso de creación de la adopción");

        if (adoption == null) {
            throw new IllegalOperationException("Adoption can not be null.");
        }

        // Verificar que el adoptante existe en BD, si se proporciona
        if (adoption.getAdopter() != null && adoption.getAdopter().getId() != null) {
            Long adopterId = Objects.requireNonNull(adoption.getAdopter().getId(), "Adopter id cannot be null");
            Optional<AdopterEntity> adopter = adopterRepository.findById(adopterId);
            if (adopter.isEmpty()) {
                throw new IllegalOperationException("Adopter does not exist.");
            }
            adoption.setAdopter(adopter.get());
        }

        // Verificar que la mascota existe en BD, si se proporciona
        if (adoption.getPet() != null && adoption.getPet().getId() != null) {
            Long petId = Objects.requireNonNull(adoption.getPet().getId(), "Pet id cannot be null");
            Optional<PetEntity> pet = petRepository.findById(petId);
            if (pet.isEmpty()) {
                throw new IllegalOperationException("Pet does not exist.");
            }
            adoption.setPet(pet.get());
        }

        adoption.setStatus(AdoptionStatus.REQUESTED);
        adoption.setContractSigned(false);

        log.info("Termina proceso de creación de la adopción");
        return adoptionRepository.save(adoption);
    }

    @Transactional
    public AdoptionEntity searchAdoption(Long id) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la adopción con id = {}", id);
        AdoptionEntity adoptionEntity = getAdoptionOrThrow(id);

        log.info("Termina proceso de consultar la adopción con id = {}", id);
        return adoptionEntity;
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
        AdoptionEntity adoption = getAdoptionOrThrow(id);

        if (adoption.getStatus() != AdoptionStatus.REQUESTED) {
            throw new IllegalOperationException("Adoption already requested or in invalid state");
        }

        adoption.setStatus(AdoptionStatus.REQUESTED);
        return adoptionRepository.save(adoption);
    }

    @Transactional
    public AdoptionEntity approveAdoption(Long id) throws EntityNotFoundException, IllegalOperationException {
        AdoptionEntity adoption = getAdoptionOrThrow(id);

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
        AdoptionEntity adoption = getAdoptionOrThrow(id);

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

        AdoptionEntity adoption = getAdoptionOrThrow(id);

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

        if (adoption.getPet() != null) {
            throw new IllegalOperationException("Cannot delete adoption with associated pet.");
        }

        if (adoption.getTrialCohabitation() != null) {
            throw new IllegalOperationException("Cannot delete adoption with associated trial cohabitation.");
        }

        adoptionRepository.delete(adoption);
        log.info("Termina proceso de borrar la adopción con id = {}", id);
    }

    private AdoptionEntity getAdoptionOrThrow(Long id) throws EntityNotFoundException {
        Long safeId = Objects.requireNonNull(id, ADOPTION_ID_NULL_MSG);
        return adoptionRepository.findById(safeId)
                .orElseThrow(() -> new EntityNotFoundException(AD_NOT_FOUND));
    }
}