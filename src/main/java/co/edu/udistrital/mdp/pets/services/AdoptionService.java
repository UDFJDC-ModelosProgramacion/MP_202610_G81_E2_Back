package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdoptionService {
    private static final String NOT_FOUND_MSG = "Adoption not found.";

    private static final String AD_NOT_FOUND = NOT_FOUND_MSG;

    @Autowired
    AdoptionRepository adoptionRepository;

    @Transactional
    public AdoptionEntity createAdoption(AdoptionEntity adoption) throws IllegalOperationException {
        log.info("Inicia proceso de creación de la adopción");

        if (adoption == null) {
            throw new IllegalOperationException("Adoption can not be null.");
        }

        log.info("Termina proceso de creación de la adopción");
        return adoptionRepository.save(adoption);
    }

    @Transactional
    public AdoptionEntity searchAdoption(Long id) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la adopción con id = {}", id);

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
    public void deleteAdoption(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar la adopción con id = {}", id);

        Optional<AdoptionEntity> adoptionEntity = adoptionRepository.findById(id);
        if (adoptionEntity.isEmpty())
            throw new EntityNotFoundException(AD_NOT_FOUND);

        AdoptionEntity adoption = adoptionEntity.get();

        if (adoption.getTrialCohabitation() != null) {
            throw new IllegalOperationException("Cannot delete adoption with associated trial cohabitation.");
        }

        if (adoption.getPet() != null) {
            throw new IllegalOperationException("Cannot delete adoption with associated pet.");
        }

        adoptionRepository.delete(adoption);
        log.info("Termina proceso de borrar la adopción con id = {}", id);
    }
}
