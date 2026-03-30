package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.InfectionEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.InfectionRepository;

@Service
public class InfectionService {

    @Autowired
    private InfectionRepository infectionRepository;

    @Transactional
    public InfectionEntity createInfection(InfectionEntity infection) throws IllegalOperationException {
        if (infection == null) throw new IllegalOperationException("Infection cannot be null");
        return infectionRepository.save(infection);
    }

    @Transactional
    public InfectionEntity searchInfection(Long id) throws EntityNotFoundException {
        Objects.requireNonNull(id, "Id cannot be null");
        Optional<InfectionEntity> entity = infectionRepository.findById(id);
        if (entity.isEmpty()) throw new EntityNotFoundException("Infection not found");
        return entity.get();
    }

    @Transactional
    public List<InfectionEntity> searchInfections() {
        return infectionRepository.findAll();
    }

    @Transactional
    public InfectionEntity updateInfection(Long id, InfectionEntity infection) throws EntityNotFoundException, IllegalOperationException {
        Objects.requireNonNull(id, "Id cannot be null");
        Optional<InfectionEntity> entity = infectionRepository.findById(id);
        if (entity.isEmpty()) throw new EntityNotFoundException("Infection not found");

        InfectionEntity existing = entity.get();
        existing.setInfectionType(infection.getInfectionType());
        existing.setDescription(infection.getDescription());
        existing.setTreatment(infection.getTreatment());
        existing.setDiagnosisDate(infection.getDiagnosisDate());
        existing.setRecoveryDate(infection.getRecoveryDate());
        
        return infectionRepository.save(existing);
    }

    @Transactional
    public void deleteInfection(Long id) throws EntityNotFoundException {
        Objects.requireNonNull(id, "Id cannot be null");
        Optional<InfectionEntity> entity = infectionRepository.findById(id);
        if (entity.isEmpty()) throw new EntityNotFoundException("Infection not found");
        infectionRepository.delete(entity.get());
    }
}
