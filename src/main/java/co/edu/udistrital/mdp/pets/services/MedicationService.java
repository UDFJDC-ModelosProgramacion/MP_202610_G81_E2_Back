package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MedicationEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MedicationRepository;

@Service
public class MedicationService {

    @Autowired
    private MedicationRepository medicationRepository;

    @Transactional
    public MedicationEntity createMedication(MedicationEntity medication) throws IllegalOperationException {
        if (medication == null) throw new IllegalOperationException("Medication cannot be null");
        return medicationRepository.save(medication);
    }

    @Transactional
    public MedicationEntity searchMedication(Long id) throws EntityNotFoundException {
        Objects.requireNonNull(id, "Id cannot be null");
        Optional<MedicationEntity> entity = medicationRepository.findById(id);
        if (entity.isEmpty()) throw new EntityNotFoundException("Medication not found");
        return entity.get();
    }

    @Transactional
    public List<MedicationEntity> searchMedications() {
        return medicationRepository.findAll();
    }

    @Transactional
    public MedicationEntity updateMedication(Long id, MedicationEntity medication) throws EntityNotFoundException, IllegalOperationException {
        Objects.requireNonNull(id, "Id cannot be null");
        Optional<MedicationEntity> entity = medicationRepository.findById(id);
        if (entity.isEmpty()) throw new EntityNotFoundException("Medication not found");

        MedicationEntity existing = entity.get();
        existing.setMedicationName(medication.getMedicationName());
        existing.setDosage(medication.getDosage());
        existing.setFrequency(medication.getFrequency());
        existing.setStartDate(medication.getStartDate());
        existing.setEndDate(medication.getEndDate());
        existing.setNotes(medication.getNotes());
        
        return medicationRepository.save(existing);
    }

    @Transactional
    public void deleteMedication(Long id) throws EntityNotFoundException {
        Objects.requireNonNull(id, "Id cannot be null");
        Optional<MedicationEntity> entity = medicationRepository.findById(id);
        if (entity.isEmpty()) throw new EntityNotFoundException("Medication not found");
        medicationRepository.delete(entity.get());
    }
}
