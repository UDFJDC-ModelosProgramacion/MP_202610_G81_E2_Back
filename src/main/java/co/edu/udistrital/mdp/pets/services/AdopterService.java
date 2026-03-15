package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdopterService {

    @Autowired
    private AdopterRepository adopterRepository;

    @Transactional
    public AdopterEntity createAdopter(AdopterEntity adopterEntity) throws IllegalOperationException {
        log.info("Inicia proceso de creación del adoptante");
        validateAdopterData(adopterEntity);
        if (!adopterRepository.findByEmail(adopterEntity.getEmail()).isEmpty()) {
            throw new IllegalOperationException("Email already exists");
        }
        log.info("Termina proceso de creación del adoptante");
        return adopterRepository.save(adopterEntity);
    }

    @Transactional
    public AdopterEntity searchAdopter(Long adopterId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar el adoptante con id = {}", adopterId);
        Optional<AdopterEntity> adopterEntity = adopterRepository.findById(adopterId);
        if (adopterEntity.isEmpty()) {
            throw new EntityNotFoundException("The adopter with the given id was not found");
        }
        log.info("Termina proceso de consultar el adoptante con id = {}", adopterId);
        return adopterEntity.get();
    }

    @Transactional
    public List<AdopterEntity> searchAdopters(String name, String email, String status) {
        log.info("Inicia proceso de consultar adoptantes por filtros");
        return adopterRepository.searchAdopters(name, email, status);
    }

    @Transactional
    public List<AdopterEntity> searchAdopters() {
        log.info("Inicia proceso de consultar todos los adoptantes");
        return adopterRepository.findAll();
    }

    @Transactional
    public AdopterEntity updateAdopter(Long adopterId, AdopterEntity adopterEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar el adoptante con id = {}", adopterId);
        Optional<AdopterEntity> persistedAdopter = adopterRepository.findById(adopterId);
        if (persistedAdopter.isEmpty()) {
            throw new EntityNotFoundException("The adopter with the given id was not found");
        }
        validateAdopterData(adopterEntity);
        boolean repeatedEmail = adopterRepository.findByEmail(adopterEntity.getEmail()).stream()
                .anyMatch(adopter -> !adopter.getId().equals(adopterId));
        if (repeatedEmail) {
            throw new IllegalOperationException("Email already exists");
        }
        adopterEntity.setId(adopterId);
        adopterEntity.setSentMessages(persistedAdopter.get().getSentMessages());
        adopterEntity.setReceivedMessages(persistedAdopter.get().getReceivedMessages());
        adopterEntity.setNotifications(persistedAdopter.get().getNotifications());
        log.info("Termina proceso de actualizar el adoptante con id = {}", adopterId);
        return adopterRepository.save(adopterEntity);
    }

    @Transactional
    public void deleteAdopter(Long adopterId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar el adoptante con id = {}", adopterId);
        Optional<AdopterEntity> adopterEntity = adopterRepository.findById(adopterId);
        if (adopterEntity.isEmpty()) {
            throw new EntityNotFoundException("The adopter with the given id was not found");
        }
        AdopterEntity adopter = adopterEntity.get();
        if (!adopter.getAdoptionRequests().isEmpty()) {
            throw new IllegalOperationException("The adopter has active adoption requests and cannot be deleted");
        }
        log.info("Termina proceso de borrar el adoptante con id = {}", adopterId);
        adopterRepository.deleteById(adopterId);
    }

    private void validateAdopterData(AdopterEntity adopterEntity) throws IllegalOperationException {
        if (adopterEntity == null) {
            throw new IllegalOperationException("Adopter is not valid");
        }
        if (adopterEntity.getName() == null || adopterEntity.getName().isBlank()) {
            throw new IllegalOperationException("Adopter name is not valid");
        }
        if (adopterEntity.getEmail() == null || adopterEntity.getEmail().isBlank()) {
            throw new IllegalOperationException("Adopter email is not valid");
        }
        if (adopterEntity.getPassword() == null || adopterEntity.getPassword().isBlank()) {
            throw new IllegalOperationException("Adopter password is not valid");
        }
        if (adopterEntity.getPhoneNumber() == null || adopterEntity.getPhoneNumber().isBlank()) {
            throw new IllegalOperationException("Adopter phone is not valid");
        }
        if (adopterEntity.getAddress() == null || adopterEntity.getAddress().isBlank()) {
            throw new IllegalOperationException("Adopter address is not valid");
        }
        if (adopterEntity.getCity() == null || adopterEntity.getCity().isBlank()) {
            throw new IllegalOperationException("Adopter city is not valid");
        }
    }
}
