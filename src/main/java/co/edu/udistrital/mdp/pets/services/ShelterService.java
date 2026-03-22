package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ShelterService {

    @Autowired
    private ShelterRepository shelterRepository;

    @Autowired
    private PetRepository petRepository;

    @Transactional
    public ShelterEntity createShelter(ShelterEntity shelter) throws IllegalOperationException {
        log.info("Creating shelter");

        if (shelter.getNit() == null || shelter.getNit().trim().isEmpty()) {
            throw new IllegalOperationException("El NIT del refugio es obligatorio");
        }
        if (shelter.getShelterName() == null || shelter.getShelterName().trim().isEmpty()) {
            throw new IllegalOperationException("El nombre del refugio es obligatorio");
        }
        if (shelter.getPhoneNumber() == null || shelter.getPhoneNumber().trim().isEmpty()) {
            throw new IllegalOperationException("El teléfono es obligatorio");
        }
        if (shelter.getAddress() == null || shelter.getAddress().trim().isEmpty()) {
            throw new IllegalOperationException("La dirección es obligatoria");
        }

        Optional<ShelterEntity> existingByNit = shelterRepository.findByNit(shelter.getNit());
        if (existingByNit.isPresent()) {
            throw new IllegalOperationException("Ya existe un refugio con el mismo NIT");
        }

        List<ShelterEntity> existingByName = shelterRepository.findByShelterName(shelter.getShelterName());
        if (!existingByName.isEmpty()) {
            throw new IllegalOperationException("Ya existe un refugio con el mismo nombre");
        }

        if (shelter.getStatus() == null || shelter.getStatus().trim().isEmpty()) {
            shelter.setStatus("Activo");
        }

        return shelterRepository.save(shelter);
    }

    @Transactional(readOnly = true)
    public ShelterEntity searchShelter(Long id) throws EntityNotFoundException {
        log.info("Searching shelter with id: {}", id);
        return shelterRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("El refugio con ID " + id + " no fue encontrado"));
    }

    @Transactional(readOnly = true)
    public List<ShelterEntity> searchSheltersByName(String name) {
        log.info("Searching shelters with name: {}", name);
        return shelterRepository.findByShelterName(name); // Retorna lista vacía si no hay coincidencias
    }

    @Transactional(readOnly = true)
    public List<ShelterEntity> searchShelters() {
        log.info("Searching all shelters");
        return shelterRepository.findAll();
    }

    @Transactional
    public ShelterEntity updateShelter(Long id, ShelterEntity shelter)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Updating shelter with id: {}", id);
        ShelterEntity existing = shelterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("El refugio con ID " + id + " no fue encontrado"));

        if (shelter.getNit() != null && !existing.getNit().equals(shelter.getNit())) {
            throw new IllegalOperationException("No se permite la modificación del NIT una vez creado");
        }

        if (!"Activo".equalsIgnoreCase(existing.getStatus())) {
            throw new IllegalOperationException(
                    "El refugio debe estar en estado 'Activo' para recibir actualizaciones");
        }

        if (shelter.getShelterName() != null && !shelter.getShelterName().equals(existing.getShelterName())) {
            List<ShelterEntity> existingByName = shelterRepository.findByShelterName(shelter.getShelterName());
            if (!existingByName.isEmpty()) {
                throw new IllegalOperationException("Ya existe un refugio con el mismo nombre");
            }
        }

        existing.setShelterName(shelter.getShelterName());
        existing.setPhoneNumber(shelter.getPhoneNumber());
        existing.setAddress(shelter.getAddress());
        existing.setCity(shelter.getCity());
        existing.setLocationDetails(shelter.getLocationDetails());
        existing.setDescription(shelter.getDescription());
        existing.setWebsiteUrl(shelter.getWebsiteUrl());
        existing.setStatus(shelter.getStatus());

        return shelterRepository.save(existing);
    }

    @Transactional
    public void deleteShelter(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("Deleting shelter with id: {}", id);
        
        shelterRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("El refugio con ID " + id + " no fue encontrado")); // Valida que exista el refugio

        List<PetEntity> pets = petRepository.findByShelterId(id);
        if (pets != null && !pets.isEmpty()) {
            throw new IllegalOperationException("No se puede eliminar un refugio que tenga mascotas registradas");
        }

        shelterRepository.deleteById(id);
    }
}
