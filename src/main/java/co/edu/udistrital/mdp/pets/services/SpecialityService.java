package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.VetSpecialityEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.VetSpecialityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SpecialityService {

    @Autowired
    private VetSpecialityRepository specialityRepository;

    @Transactional
    public VetSpecialityEntity createSpeciality(VetSpecialityEntity speciality) throws IllegalOperationException {
        log.info("Creating speciality");

        if (speciality.getName() == null || speciality.getName().trim().isEmpty()) {
             throw new IllegalOperationException("El nombre de la especialidad es obligatorio");
        }

        Optional<VetSpecialityEntity> existing = specialityRepository.findByNameIgnoreCase(speciality.getName().trim());
        if (existing.isPresent()) {
            throw new IllegalOperationException("Ya existe una especialidad con el nombre " + speciality.getName());
        }

        return specialityRepository.save(speciality);
    }

    @SuppressWarnings("null")
    @Transactional(readOnly = true)
    public VetSpecialityEntity searchSpeciality(Long id) throws EntityNotFoundException {
        log.info("Searching speciality with id: {}", id);
        return specialityRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Especialidad no encontrada"));
    }

    @Transactional(readOnly = true)
    public List<VetSpecialityEntity> searchSpecialities(String namePart) {
        log.info("Searching specialities by name part: {}", namePart);
        if (namePart == null || namePart.trim().isEmpty()) {
            return specialityRepository.findAll();
        }
        return specialityRepository.findByNameContainingIgnoreCase(namePart.trim());
    }

    @SuppressWarnings("null")
    @Transactional
    public VetSpecialityEntity updateSpeciality(Long id, VetSpecialityEntity speciality) throws EntityNotFoundException, IllegalOperationException {
        log.info("Updating speciality with id: {}", id);
        VetSpecialityEntity existing = searchSpeciality(id);

        if (speciality.getName() != null && !speciality.getName().equalsIgnoreCase(existing.getName())) {
            Optional<VetSpecialityEntity> duplicate = specialityRepository.findByNameIgnoreCase(speciality.getName().trim());
            if (duplicate.isPresent()) {
                throw new IllegalOperationException("Ya existe una especialidad con el nombre " + speciality.getName());
            }
            existing.setName(speciality.getName());
        }

        if (speciality.getDescription() != null) {
            existing.setDescription(speciality.getDescription());
        }

        return specialityRepository.save(existing);
    }

    @SuppressWarnings("null")
    @Transactional
    public void deleteSpeciality(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting speciality with id: {}", id);
        VetSpecialityEntity existing = searchSpeciality(id);

        if (existing.getVeterinarians() != null && !existing.getVeterinarians().isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar la especialidad porque hay veterinarios vinculados a ella");
        }

        specialityRepository.deleteById(id);
    }
}
