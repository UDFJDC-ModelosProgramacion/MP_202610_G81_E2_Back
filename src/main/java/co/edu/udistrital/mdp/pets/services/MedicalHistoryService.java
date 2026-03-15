package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MedicalHistoryRepository;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MedicalHistoryService {
          @Autowired
          private MedicalHistoryRepository mhRepository;

          @Transactional
          public MedicalHistoryEntity createPet(MedicalHistoryEntity mh) throws IllegalOperationException {
                    log.info("Creating Pet");

                    return mhRepository.save(mh);
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