package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import co.edu.udistrital.mdp.pets.repositories.UserRepository;
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
            throws IllegalOperationException {
        log.info("Creating veterinarian");

        validateVeterinarianData(veterinarian);

        if (veterinarian.getLicenseNumber() == null || veterinarian.getLicenseNumber().trim().isEmpty()) {
            throw new IllegalOperationException("El número de matrícula profesional es obligatorio");
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
        
        Optional<VeterinarianEntity> existing = veterinarianRepository.findById(id);
        if (existing.isEmpty()) {
            throw notFound(id);
        }

        validateVeterinarianData(veterinarian);

        VeterinarianEntity existingVet = existing.get();
        existingVet.setName(veterinarian.getName());
        existingVet.setEmail(veterinarian.getEmail());
        existingVet.setPassword(veterinarian.getPassword());
        existingVet.setPhoneNumber(veterinarian.getPhoneNumber());
        existingVet.setProfileImageUrl(veterinarian.getProfileImageUrl());
        existingVet.setRegisterDate(veterinarian.getRegisterDate());
        existingVet.setLicenseNumber(veterinarian.getLicenseNumber());
        existingVet.setAvailabilitySchedule(veterinarian.getAvailabilitySchedule());
        
        if (veterinarian.getShelter() != null) {
            existingVet.setShelter(veterinarian.getShelter());
        }
        
        if (veterinarian.getSpecialities() != null) {
            existingVet.setSpecialities(veterinarian.getSpecialities());
        }

        return veterinarianRepository.save(existingVet);
    }

    @Transactional
    public void deleteVeterinarian(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting veterinarian with id: {}", id);
        Objects.requireNonNull(id, VETERINARIAN_ID_NULL_MSG);
        
        Optional<VeterinarianEntity> existing = veterinarianRepository.findById(id);
        if (existing.isEmpty()) {
            throw notFound(id);
        }

        VeterinarianEntity veterinarian = existing.get();

        if (veterinarian.getMedicalHistories() != null && !veterinarian.getMedicalHistories().isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar porque es responsable de una historia clínica");
        }

        if (veterinarian.getMedicalEvents() != null && !veterinarian.getMedicalEvents().isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar porque tiene eventos médicos pendientes");
        }

        veterinarianRepository.deleteById(id);
    }

    private void validateVeterinarianData(VeterinarianEntity veterinarian) throws IllegalOperationException {
        if (veterinarian == null) {
            throw new IllegalOperationException("Veterinarian is not valid");
        }
        if (veterinarian.getName() == null || veterinarian.getName().isBlank()) {
            throw new IllegalOperationException("Veterinarian name is not valid");
        }
        if (veterinarian.getEmail() == null || veterinarian.getEmail().isBlank()) {
            throw new IllegalOperationException("Veterinarian email is not valid");
        }
        if (veterinarian.getPassword() == null || veterinarian.getPassword().isBlank()) {
            throw new IllegalOperationException("Veterinarian password is not valid");
        }
        if (veterinarian.getPhoneNumber() == null || veterinarian.getPhoneNumber().isBlank()) {
            throw new IllegalOperationException("Veterinarian phone is not valid");
        }
    }

    private EntityNotFoundException notFound(Long id) {
        return new EntityNotFoundException(VET_ID_PREFIX + id + NOT_FOUND_SUFFIX);
    }
}