package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.repositories.MedicalEventRepository;
import co.edu.udistrital.mdp.pets.repositories.MedicalHistoryRepository;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class MedicalEventService {
          private static final String MEDICAL_EVENT_ID_NULL_MSG = "Medical event id cannot be null";

          @Autowired
          private MedicalEventRepository eventRepository;
          @Autowired
          private MedicalHistoryRepository historyRepository;
          @Autowired
          private VeterinarianRepository veterinarianRepository;

          /**
           * Cada vez que haya un problema medico se crea un evento
           * @throws EntityNotFoundException 
           */
          @Transactional
          public MedicalEventEntity createMedicalEvent(
                              Long historyId,
                              Long veterinarianId,
                              MedicalEventEntity event) throws EntityNotFoundException {
                    Objects.requireNonNull(historyId, "Medical history id cannot be null");
                    Objects.requireNonNull(veterinarianId, "Veterinarian id cannot be null");

                    MedicalHistoryEntity history = historyRepository.findById(historyId)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                            "Medical history not found"));

                    VeterinarianEntity vet = veterinarianRepository.findById(veterinarianId)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                            "Veterinarian not found"));

                    event.setMedicalHistory(history);
                    event.setVeterinarian(vet);

                    history.setLastUpdated(LocalDate.now());

                    return eventRepository.save(event);
          }

          @Transactional(readOnly = true)
          public MedicalEventEntity searchMedicalEvent(Long eventId) throws EntityNotFoundException {
                    Objects.requireNonNull(eventId, MEDICAL_EVENT_ID_NULL_MSG);

                    return eventRepository.findById(eventId)
                                        .orElseThrow(() -> new EntityNotFoundException(
                                                            "Medical event not found"));
          }

          @Transactional(readOnly = true)
          public List<MedicalEventEntity> searchMedicalEvents(LocalDate startDate, LocalDate endDate) {

                    return eventRepository.findByEventDateBetween(startDate, endDate);
          }

          @Transactional
          public MedicalEventEntity updateMedicalEvent(Long eventId, MedicalEventEntity updatedEvent) throws EntityNotFoundException {
                    Objects.requireNonNull(eventId, MEDICAL_EVENT_ID_NULL_MSG);

                    MedicalEventEntity event = eventRepository.findById(eventId).orElseThrow(() -> new EntityNotFoundException("Medical event not found"));

                    event.setEventType(updatedEvent.getEventType());
                    event.setDescription(updatedEvent.getDescription());
                    event.setEventDate(updatedEvent.getEventDate());

                    return eventRepository.save(event);
          }

          @Transactional
          public void deleteMedicalEvent(Long eventId) {
                    Objects.requireNonNull(eventId, MEDICAL_EVENT_ID_NULL_MSG);

                    eventRepository.deleteById(eventId);
          }
}
