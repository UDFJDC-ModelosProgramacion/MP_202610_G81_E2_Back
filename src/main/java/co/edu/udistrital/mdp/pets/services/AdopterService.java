package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.AdopterRepository;
import jakarta.transaction.Transactional;
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
        return adopterRepository.save(adopterEntity);
    }

    @Transactional
    public AdopterEntity searchAdopter(Long adopterId) throws EntityNotFoundException {
        Optional<AdopterEntity> adopterEntity = adopterRepository.findById(adopterId);
        if (adopterEntity.isEmpty()) {
            throw new EntityNotFoundException("The adopter with the given id was not found");
        }
        return adopterEntity.get();
    }

    @Transactional
    public List<AdopterEntity> searchAdopters() {
        return adopterRepository.findAll();
    }

    @Transactional
    public AdopterEntity updateAdopter(Long adopterId, AdopterEntity adopterEntity)
            throws EntityNotFoundException, IllegalOperationException {
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
        return adopterRepository.save(adopterEntity);
    }

    @Transactional
    public void deleteAdopter(Long adopterId) throws EntityNotFoundException, IllegalOperationException {
        Optional<AdopterEntity> adopterEntity = adopterRepository.findById(adopterId);
        if (adopterEntity.isEmpty()) {
            throw new EntityNotFoundException("The adopter with the given id was not found");
        }
        AdopterEntity adopter = adopterEntity.get();
        if (!adopter.getSentMessages().isEmpty() || !adopter.getReceivedMessages().isEmpty()
                || !adopter.getNotifications().isEmpty()) {
            throw new IllegalOperationException("The adopter has active associations and cannot be deleted");
        }
        adopterRepository.deleteById(adopterId);
    }

    private void validateAdopterData(AdopterEntity adopterEntity) throws IllegalOperationException {
        if (adopterEntity == null) {
            throw new IllegalOperationException("Adopter is not valid");
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
    }
}
