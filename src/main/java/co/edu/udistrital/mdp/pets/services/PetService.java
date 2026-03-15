package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PetService {
          @Autowired
          private PetRepository petRepository;

          @Transactional
          public PetEntity createPet(PetEntity pet) throws IllegalOperationException {
                    log.info("Creating Pet");

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
                              throw new IllegalOperationException(
                                                  "Las necesitades especificas de la mascota son obligatorias");
                    }
                    if (pet.getStatus() == null || pet.getStatus().trim().isEmpty()) {
                              throw new IllegalOperationException("El estado general de la mascota es obligatorio");
                    }
                    if (pet.getStatus() == null || pet.getStatus().trim().isEmpty()) {
                              throw new IllegalOperationException("El estado general de la mascota es obligatorio");
                    }
                    return petRepository.save(pet);
          }

          @Transactional(readOnly = true)
          public PetEntity searchPetEntity(Long id) throws EntityNotFoundException{
                    log.info("Searching pet by id: {}", id );
                    return petRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("No hay una mascota con el id " + id + "en el shelter"));
          }

          @Transactional(readOnly = true)
          public List<PetEntity> searchPetEntitiesByShelter(Long shelterid) throws EntityNotFoundException{
                    log.info("Searching pet by shelter: {}", shelterid);
                    return petRepository.findByShelterId(shelterid);
                    //.orElseThrow(() -> new EntityNotFoundException("No hay mascotas en el refugio o no hay una mascota con el id "+ id));
          }

          @Transactional
          public PetEntity updatePet(Long id, PetEntity petEntity) throws EntityNotFoundException, IllegalOperationException {
                    log.info("Updating pet with id: {}", id);
                    PetEntity existing = searchPetEntity(id);

                    if (petEntity.getId() != null && !existing.getId().equals(petEntity.getId())) {
                              throw new IllegalOperationException("No se puede desvincular del UserEntity original");
                    }

                    existing.setName(petEntity.getName());
                    existing.setBreed(petEntity.getBreed());
                    existing.setBornDate(petEntity.getBornDate());
                    existing.setSex(petEntity.getSex());
                    existing.setSize(petEntity.getSize());
                    existing.setTemperament(petEntity.getTemperament());
                    existing.setSpecificNeeds(petEntity.getSpecificNeeds());
                    existing.setRescued(petEntity.isRescued());
                    existing.setStatus(petEntity.getStatus());

                    return petRepository.save(existing);
          }

          @Transactional
          public void deletePet(Long id){

          }

}
