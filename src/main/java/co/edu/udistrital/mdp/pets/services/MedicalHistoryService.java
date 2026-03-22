package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MedicalEventRepository;
import co.edu.udistrital.mdp.pets.repositories.MedicalHistoryRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicalHistoryService {
    private static final String NOT_FOUND_MSG = NOT_FOUND_MSG;

    private static final String MED_HIST_NOT_FOUND = NOT_FOUND_MSG;

          @Autowired
          private MedicalHistoryRepository historyRepository;
          @Autowired
          private PetRepository petRepository;
          @Autowired
          private VeterinarianRepository veterinarianRepository;

          /**
           * 1. Solo puede ser creado por un veterinario
           * 2. Necesita mascota
           */
          @Transactional
          public MedicalHistoryEntity createMedicalHistory(Long petId, Long veterinarianId) throws EntityNotFoundException {

                    PetEntity pet = petRepository.findById(petId)
                                        .orElseThrow(() -> new EntityNotFoundException("Pet not found"));

                    VeterinarianEntity vet = veterinarianRepository.findById(veterinarianId)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                            "Veterinarian not found"));

                    MedicalHistoryEntity history = new MedicalHistoryEntity();

                    history.setPet(pet);
                    history.setVeterinarian(vet);
                    history.setCreatedDate(LocalDate.now());
                    history.setLastUpdated(LocalDate.now());

                    return historyRepository.save(history);
          }

          @Transactional(readOnly = true)
          public MedicalHistoryEntity searchMedicalHistory(Long petId) throws EntityNotFoundException {

                    return historyRepository.findByPetId(petId)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                            MED_HIST_NOT_FOUND));
          }

          public List<MedicalHistoryEntity> searchMedicalHistories() {

                    return historyRepository.findAll();
          }

          /**
           * Solo se actualiza cuando hay eventos médicos
           */
          @Transactional
          public MedicalHistoryEntity updateMedicalHistory(Long historyId) throws EntityNotFoundException {

                    MedicalHistoryEntity history = historyRepository.findById(historyId)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                            MED_HIST_NOT_FOUND));

                    history.setLastUpdated(LocalDate.now());

                    return historyRepository.save(history);
          }

          /**
           * Solo se borra cuando la mascota es adoptada
           */
          @Transactional
          public void deleteMedicalHistory(Long historyId) throws EntityNotFoundException, IllegalOperationException {

                    MedicalHistoryEntity history = historyRepository.findById(historyId)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                            MED_HIST_NOT_FOUND));

                    if (!"ADOPTED".equals(history.getPet().getStatus())) {
                              throw new IllegalOperationException(
                                                  "Medical history can only be deleted after adoption");
                    }

                    historyRepository.delete(history);
          }
}