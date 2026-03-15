package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.VaccineEntryEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.MedicalHistoryRepository;
import co.edu.udistrital.mdp.pets.repositories.VaccineEntryRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VaccineEntryService {

          @Autowired
          private VaccineEntryRepository vaccineRepository;
          @Autowired
          private MedicalHistoryRepository historyRepository;

          /**
           * Se crea cuando se vacuna una mascota
           * @throws EntityNotFoundException 
           */
          @Transactional
          public VaccineEntryEntity createVaccineEntry(
                              Long medicalHistoryId,
                              VaccineEntryEntity entry) throws EntityNotFoundException {

                    MedicalHistoryEntity history = historyRepository.findById(medicalHistoryId)
                                        .orElseThrow(() -> new EntityNotFoundException("Medical history not found"));

                    entry.setMedicalHistory(history);

                    history.setLastUpdated(LocalDate.now());

                    return vaccineRepository.save(entry);
          }

          public List<VaccineEntryEntity> getVaccineEntries(Long medicalHistoryId) {

                    return vaccineRepository.findByMedicalHistoryId(medicalHistoryId);
          }

          public VaccineEntryEntity updateVaccineEntry(
                              Long entryId,
                              VaccineEntryEntity updatedEntry) throws EntityNotFoundException {

                    VaccineEntryEntity entry = vaccineRepository.findById(entryId)
                                        .orElseThrow(() -> new EntityNotFoundException("Vaccine entry not found"));

                    entry.setVaccineName(updatedEntry.getVaccineName());
                    entry.setAdminDate(updatedEntry.getAdminDate());
                    entry.setNextDueDate(updatedEntry.getNextDueDate());

                    return vaccineRepository.save(entry);
          }

          /**
           * Buscar vacunas pendientes
           */
          public List<VaccineEntryEntity> findVaccinesDue() {

                    return vaccineRepository.findByNextDueDateBefore(LocalDate.now());
          }
}