package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MedicalHistoryRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicalHistoryService {
    private static final String MED_HIST_NOT_FOUND = "Medical history not found";
    private static final String PET_NOT_FOUND = "Pet not found";
    private static final String VET_NOT_FOUND = "Veterinarian not found";

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
                    Objects.requireNonNull(petId, "Pet id cannot be null");
                    Objects.requireNonNull(veterinarianId, "Veterinarian id cannot be null");

                    PetEntity pet = petRepository.findById(petId)
                                        .orElseThrow(() -> new EntityNotFoundException(PET_NOT_FOUND));

                    VeterinarianEntity vet = veterinarianRepository.findById(veterinarianId)
                                        .orElseThrow(() -> new EntityNotFoundException(VET_NOT_FOUND));

                    MedicalHistoryEntity history = new MedicalHistoryEntity();

                    history.setPet(pet);
                    history.setVeterinarian(vet);
                    history.setCreatedDate(LocalDate.now());
                    history.setLastUpdated(LocalDate.now());

                    return historyRepository.save(history);
          }

          @Transactional(readOnly = true)
          public MedicalHistoryEntity searchMedicalHistory(Long petId) throws EntityNotFoundException {
                    Objects.requireNonNull(petId, "Pet id cannot be null");

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
                    Objects.requireNonNull(historyId, "Medical history id cannot be null");

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
                    Objects.requireNonNull(historyId, "Medical history id cannot be null");

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