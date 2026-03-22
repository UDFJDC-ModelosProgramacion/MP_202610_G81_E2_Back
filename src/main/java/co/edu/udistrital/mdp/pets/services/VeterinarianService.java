package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.UserRepository;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class VeterinarianService {

    @Autowired
    private VeterinarianRepository veterinarianRepository;

    @Autowired
    private UserRepository userRepository;

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

        // Verifica si existe el usuario original
        if (!userRepository.existsById(veterinarian.getId())) {
            throw new EntityNotFoundException("El UserEntity base asociado no existe en el sistema");
        }

        return veterinarianRepository.save(veterinarian);
    }

    @Transactional(readOnly = true)
    public VeterinarianEntity searchVeterinarian(Long id) throws EntityNotFoundException {
        log.info("Searching veterinarian with id: {}", id);
        return veterinarianRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El veterinario con ID " + id + " no fue encontrado"));
    }

    @Transactional(readOnly = true)
    public List<VeterinarianEntity> searchVeterinarians() {
        log.info("Searching all veterinarians");
        return veterinarianRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<VeterinarianEntity> searchVeterinariansBySpeciality(Long specialityId) {
        log.info("Searching veterinarians by speciality: {}", specialityId);
        return veterinarianRepository.findBySpecialitiesId(specialityId);
    }

    @Transactional(readOnly = true)
    public List<VeterinarianEntity> searchVeterinariansByShelter(Long shelterId) {
        log.info("Searching veterinarians by shelter: {}", shelterId);
        return veterinarianRepository.findByShelterId(shelterId);
    }

    @Transactional
    public VeterinarianEntity updateVeterinarian(Long id, VeterinarianEntity veterinarian)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Updating veterinarian with id: {}", id);
        VeterinarianEntity existing = veterinarianRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("El veterinario con ID " + id + " no fue encontrado"));

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
        VeterinarianEntity existing = veterinarianRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("El veterinario con ID " + id + " no fue encontrado"));

        if (existing.getMedicalHistories() != null && !existing.getMedicalHistories().isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar porque es responsable de una historia clínica");
        }

        if (existing.getMedicalEvents() != null && !existing.getMedicalEvents().isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar porque tiene eventos médicos pendientes");
        }

        veterinarianRepository.deleteById(id);
    }
}
