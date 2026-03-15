package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetService {

          @Autowired
          private PetRepository petRepository;
          @Autowired
          private ShelterRepository shelterRepository;

          /**
           * Regla:
           * 1. La mascota llega a un shelter
           * 2. No puede existir duplicada
           */
          @Transactional
          public PetEntity createPet(Long shelterId, PetEntity pet) {
                    log.info("Creating pet");

                    boolean exists = petRepository.existsByNameAndBreedAndBornDate(
                                        pet.getName(),
                                        pet.getBreed(),
                                        pet.getBornDate());

                    if (exists) {
                              throw new IllegalStateException("Pet already exists");
                    }

                    ShelterEntity shelter = shelterRepository.findById(shelterId)
                                        .orElseThrow(() -> new EntityNotFoundException("Shelter not found"));

                    pet.setShelter(shelter);
                    pet.setStatus("IN_SHELTER");

                    return petRepository.save(pet);
          }

          @Transactional(readOnly = true)
          public PetEntity getPet(Long petId) {

                    return petRepository.findById(petId)
                                        .orElseThrow(() -> new EntityNotFoundException("Pet not found"));
          }

          @Transactional(readOnly = true)
          public List<PetEntity> searchPets(String breed, String size, String temperament) {

                    return petRepository.searchPets(breed, size, temperament);
          }

          @Transactional
          public PetEntity updatePet(Long petId, PetEntity updatedPet) {

                    PetEntity pet = getPet(petId);

                    pet.setName(updatedPet.getName());
                    pet.setBreed(updatedPet.getBreed());
                    pet.setBornDate(updatedPet.getBornDate());
                    pet.setSex(updatedPet.getSex());
                    pet.setSize(updatedPet.getSize());
                    pet.setTemperament(updatedPet.getTemperament());
                    pet.setSpecificNeeds(updatedPet.getSpecificNeeds());
                    pet.setIsRescued(updatedPet.getIsRescued());

                    return petRepository.save(pet);
          }

          /**
           * Solo se elimina si está adoptada o murió
           */
          @Transactional
          public void deletePet(Long petId) {

                    PetEntity pet = getPet(petId);

                    if (!pet.getStatus().equals("ADOPTED") &&
                                        !pet.getStatus().equals("DECEASED")) {

                              throw new IllegalStateException(
                                                  "Pet cannot be deleted unless adopted or deceased");
                    }

                    petRepository.delete(pet);
          }
}
