package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.UserRepository;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VeterinarianService {

    private static final String VET_ID_PREFIX = "El veterinario con ID ";
    private static final String NOT_FOUND_SUFFIX = " no fue encontrado";
    private static final String VETERINARIAN_ID_NULL_MSG = "Veterinarian id cannot be null";

    private final VeterinarianRepository veterinarianRepository;
    private final UserRepository userRepository;

    public VeterinarianService(VeterinarianRepository veterinarianRepository, UserRepository userRepository) {
        this.veterinarianRepository = veterinarianRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public VeterinarianEntity createVeterinarian(VeterinarianEntity veterinarian)
            throws IllegalOperationException, EntityNotFoundException {
        log.info("Creating veterinarian");

        if (veterinarian.getLicenseNumber() == null || veterinarian.getLicenseNumber().trim().isEmpty()) {
            throw new IllegalOperationException("El número de matrícula profesional es obligatorio");
        }

        if (veterinarian.getId() == null) {
            throw new IllegalOperationException(
                    "El veterinario debe estar vinculado a un UserEntity ya existente en el sistema");
        }

        Long veterinarianId = Objects.requireNonNull(veterinarian.getId(), VETERINARIAN_ID_NULL_MSG);

        // Verifica si existe el usuario original
        if (!userRepository.existsById(veterinarianId)) {
            throw new EntityNotFoundException("El UserEntity base asociado no existe en el sistema");
        }

        return veterinarianRepository.save(veterinarian);
    }

    @Transactional(readOnly = true)
    public VeterinarianEntity searchVeterinarian(Long id) throws EntityNotFoundException {
        log.info("Searching veterinarian with id: {}", id);
        Objects.requireNonNull(id, VETERINARIAN_ID_NULL_MSG);
        return veterinarianRepository.findById(id).orElseThrow(() -> notFound(id));
    }

    @Transactional(readOnly = true)
    public List<VeterinarianEntity> searchVeterinarians() {
        log.info("Searching all veterinarians");
        return veterinarianRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<VeterinarianEntity> searchVeterinariansBySpeciality(Long specialityId) {
        log.info("Searching veterinarians by speciality: {}", specialityId);
        Objects.requireNonNull(specialityId, "Speciality id cannot be null");
        return veterinarianRepository.findBySpecialitiesId(specialityId);
    }

    @Transactional(readOnly = true)
    public List<VeterinarianEntity> searchVeterinariansByShelter(Long shelterId) {
        log.info("Searching veterinarians by shelter: {}", shelterId);
        Objects.requireNonNull(shelterId, "Shelter id cannot be null");
        return veterinarianRepository.findByShelterId(shelterId);
    }

    @Transactional
    public VeterinarianEntity updateVeterinarian(Long id, VeterinarianEntity veterinarian)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Updating veterinarian with id: {}", id);
        Objects.requireNonNull(id, VETERINARIAN_ID_NULL_MSG);
        VeterinarianEntity existing = veterinarianRepository.findById(id).orElseThrow(() -> notFound(id));

        if (veterinarian.getId() != null && !existing.getId().equals(veterinarian.getId())) {
            throw new IllegalOperationException("No se puede desvincular del UserEntity original");
        }

        existing.setEmail(veterinarian.getEmail());
        existing.setPhoneNumber(veterinarian.getPhoneNumber());
        existing.setProfileImageUrl(veterinarian.getProfileImageUrl());
        existing.setAvailabilitySchedule(veterinarian.getAvailabilitySchedule());
        existing.setName(veterinarian.getName());
        existing.setSpecialities(veterinarian.getSpecialities());

        return veterinarianRepository.save(existing);
    }

    @Transactional
    public void deleteVeterinarian(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting veterinarian with id: {}", id);
        Objects.requireNonNull(id, VETERINARIAN_ID_NULL_MSG);
        VeterinarianEntity existing = veterinarianRepository.findById(id).orElseThrow(() -> notFound(id));

        if (existing.getMedicalHistories() != null && !existing.getMedicalHistories().isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar porque es responsable de una historia clínica");
        }

        if (existing.getMedicalEvents() != null && !existing.getMedicalEvents().isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar porque tiene eventos médicos pendientes");
        }

        veterinarianRepository.deleteById(id);
    }

    private EntityNotFoundException notFound(Long id) {
        return new EntityNotFoundException(VET_ID_PREFIX + id + NOT_FOUND_SUFFIX);
    }
}
