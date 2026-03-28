package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetService {

          private static final String PET_NOT_FOUND = "Pet not found";
          private static final String PET_ID_NULL_MSG = "Pet id cannot be null";

          private final PetRepository petRepository;
          private final ShelterRepository shelterRepository;

          public PetService(PetRepository petRepository, ShelterRepository shelterRepository) {
                    this.petRepository = petRepository;
                    this.shelterRepository = shelterRepository;
          }

          @Transactional
          public PetEntity createPet(Long shelterId, PetEntity pet) throws IllegalOperationException, EntityNotFoundException {
                    log.info("Creating pet");
                    Objects.requireNonNull(shelterId, "Shelter id cannot be null");

                    if (pet.getName() == null || pet.getName().trim().isEmpty()) {
                              throw new IllegalOperationException("El nombre de la mascota es obligatorio");
                    }
                    if (pet.getTemperament() == null || pet.getTemperament().trim().isEmpty()) {
                              throw new IllegalOperationException("El temperamento de la mascota es obligatorio");
                    }
                    if (pet.getBreed() == null || pet.getBreed().trim().isEmpty()) {
                              throw new IllegalOperationException("La raza de la mascota es obligatoria");
                    }
                    if (pet.getSize() == null || pet.getSize().trim().isEmpty()) {
                              throw new IllegalOperationException("El tamanio de la mascota es obligatorio");
                    }
                    if (pet.getSex() == null || pet.getSex().trim().isEmpty()) {
                              throw new IllegalOperationException("El sexo de la mascota es obligatorio");
                    }
                    if (pet.getSpecificNeeds() == null || pet.getSpecificNeeds().trim().isEmpty()) {
                              throw new IllegalOperationException("Las necesitades especificas de la mascota son obligatorias");
                    }

                    boolean exists = petRepository.existsByNameAndBreedAndBornDate(
                                        pet.getName(),
                                        pet.getBreed(),
                                        pet.getBornDate());

                    if (exists) {
                              throw new IllegalOperationException("Pet already exists");
                    }

                    ShelterEntity shelter = shelterRepository.findById(shelterId)
                                        .orElseThrow(() -> new EntityNotFoundException("Shelter not found"));

                    pet.setShelter(shelter);
                    pet.setStatus("IN_SHELTER");

                    return petRepository.save(pet);
          }

          @Transactional(readOnly = true)
          public PetEntity getPet(Long petId) throws EntityNotFoundException {
                    Objects.requireNonNull(petId, PET_ID_NULL_MSG);
                    return petRepository.findById(petId).orElseThrow(this::petNotFound);
          }

          @Transactional(readOnly = true)
          public List<PetEntity> searchPets(String breed, String size, String temperament) {
                    return petRepository.searchPets(breed, size, temperament);
          }

          @Transactional
          public PetEntity updatePet(Long petId, PetEntity updatedPet) throws EntityNotFoundException {
                    Objects.requireNonNull(petId, PET_ID_NULL_MSG);
                    PetEntity pet = petRepository.findById(petId).orElseThrow(this::petNotFound);

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
          public void deletePet(Long petId) throws EntityNotFoundException, IllegalOperationException {
                    Objects.requireNonNull(petId, PET_ID_NULL_MSG);
                    PetEntity pet = petRepository.findById(petId).orElseThrow(this::petNotFound);

                    if (!"ADOPTED".equals(pet.getStatus()) && !"DECEASED".equals(pet.getStatus())) {
                              throw new IllegalOperationException("Pet cannot be deleted unless adopted or deceased");
                    }

                    petRepository.delete(pet);
          }

          private EntityNotFoundException petNotFound() {
                    return new EntityNotFoundException(PET_NOT_FOUND);
          }
}
