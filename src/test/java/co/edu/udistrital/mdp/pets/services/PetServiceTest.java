package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import({PetService.class})
class PetServiceTest {

    @Autowired
    private PetService petService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<ShelterEntity> shelterList = new ArrayList<>();
    private final List<PetEntity> petList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
            entityManager.persist(shelter);
            shelterList.add(shelter);
        }

        for (int i = 0; i < 3; i++) {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            pet.setName("Pet " + i);
            pet.setBreed("Breed " + i);
            pet.setBornDate(LocalDate.now().minusYears(i));
            pet.setShelter(shelterList.get(0));
            pet.setStatus("IN_SHELTER");
            entityManager.persist(pet);
            petList.add(pet);
        }
    }

    @Test
    void testCreatePet() {
        PetEntity newPet = factory.manufacturePojo(PetEntity.class);
        newPet.setName("New Pet");
        newPet.setBreed("New Breed");
        newPet.setBornDate(LocalDate.now().minusDays(10));

        PetEntity result = petService.createPet(shelterList.get(0).getId(), newPet);

        assertNotNull(result);
        PetEntity stored = entityManager.find(PetEntity.class, result.getId());
        assertEquals("IN_SHELTER", stored.getStatus());
        assertEquals(shelterList.get(0).getId(), stored.getShelter().getId());
        assertEquals(newPet.getName(), stored.getName());
    }

    @Test
    void testCreatePetExists(){
        PetEntity newPet = factory.manufacturePojo(PetEntity.class);
        newPet.setName(petList.get(0).getName());
        newPet.setBreed(petList.get(0).getBreed());
        newPet.setBornDate(petList.get(0).getBornDate());

        assertNotNull(assertThrows(IllegalOperationException.class, 
            () -> petService.createPet(shelterList.get(0).getId(), newPet)));
    }

    @Test
    void testCreatePetShelterNotFound(){
        PetEntity newPet = factory.manufacturePojo(PetEntity.class);
        newPet.setName("New Pet");
        newPet.setBreed("New Breed");
        newPet.setBornDate(LocalDate.now().minusDays(10));

        assertNotNull(assertThrows(EntityNotFoundException.class, 
            () -> petService.createPet(0L, newPet)));
    }

    @Test
    void testGetPet() {
        PetEntity pet = petList.get(0);
        PetEntity result = petService.getPet(pet.getId());
        
        assertNotNull(result);
        assertEquals(pet.getId(), result.getId());
        assertEquals(pet.getName(), result.getName());
    }

    @Test
    void testGetPetNotFound() {
        assertNotNull(assertThrows(EntityNotFoundException.class, () -> petService.getPet(0L)));
    }

    @Test
    void testSearchPets() {
        PetEntity pet = petList.get(0);
        List<PetEntity> results = petService.searchPets(pet.getBreed(), pet.getSize(), pet.getTemperament());
        
        assertFalse(results.isEmpty());
        boolean found = false;
        for (PetEntity p : results) {
            if (p.getId().equals(pet.getId())) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }

    @Test
    void testUpdatePet() {
        PetEntity pet = petList.get(0);
        PetEntity newData = factory.manufacturePojo(PetEntity.class);
        newData.setName("Updated name");
        newData.setBreed("Updated breed");

        petService.updatePet(pet.getId(), newData);

        PetEntity stored = entityManager.find(PetEntity.class, pet.getId());
        assertEquals("Updated name", stored.getName());
        assertEquals("Updated breed", stored.getBreed());
        assertEquals(pet.getStatus(), stored.getStatus()); // Status shouldn't have been updated directly here
    }

    @Test
    void testUpdatePetNotFound() {
        PetEntity newData = factory.manufacturePojo(PetEntity.class);
        assertNotNull(assertThrows(EntityNotFoundException.class, () -> petService.updatePet(0L, newData)));
    }

    @Test
    void testDeletePetAdopted() {
        PetEntity pet = petList.get(0);
        pet.setStatus("ADOPTED");
        entityManager.persist(pet);

        petService.deletePet(pet.getId());
        PetEntity deleted = entityManager.find(PetEntity.class, pet.getId());
        assertNull(deleted);
    }

    @Test
    void testDeletePetDeceased() {
        PetEntity pet = petList.get(0);
        pet.setStatus("DECEASED");
        entityManager.persist(pet);

        petService.deletePet(pet.getId());
        PetEntity deleted = entityManager.find(PetEntity.class, pet.getId());
        assertNull(deleted);
    }

    @Test
    void testDeletePetInShelter(){
        PetEntity pet = petList.get(0); // Status is IN_SHELTER by default in insertData()
        
        assertNotNull(assertThrows(IllegalOperationException.class, () -> petService.deletePet(pet.getId())));
    }

    @Test
    void testCreatePetValidations() {
        PetEntity newPet = factory.manufacturePojo(PetEntity.class);
        Long shelterId = shelterList.get(0).getId();

        newPet.setName(null);
        assertThrows(IllegalOperationException.class, () -> petService.createPet(shelterId, newPet));
        newPet.setName("  ");
        assertThrows(IllegalOperationException.class, () -> petService.createPet(shelterId, newPet));
        newPet.setName("Valid Name");

        newPet.setTemperament(null);
        assertThrows(IllegalOperationException.class, () -> petService.createPet(shelterId, newPet));
        newPet.setTemperament("  ");
        assertThrows(IllegalOperationException.class, () -> petService.createPet(shelterId, newPet));
        newPet.setTemperament("Calm");

        newPet.setBreed(null);
        assertThrows(IllegalOperationException.class, () -> petService.createPet(shelterId, newPet));
        newPet.setBreed("  ");
        assertThrows(IllegalOperationException.class, () -> petService.createPet(shelterId, newPet));
        newPet.setBreed("Mix");

        newPet.setSize(null);
        assertThrows(IllegalOperationException.class, () -> petService.createPet(shelterId, newPet));
        newPet.setSize("  ");
        assertThrows(IllegalOperationException.class, () -> petService.createPet(shelterId, newPet));
        newPet.setSize("Medium");

        newPet.setSex(null);
        assertThrows(IllegalOperationException.class, () -> petService.createPet(shelterId, newPet));
        newPet.setSex("  ");
        assertThrows(IllegalOperationException.class, () -> petService.createPet(shelterId, newPet));
        newPet.setSex("Male");

        newPet.setSpecificNeeds(null);
        assertThrows(IllegalOperationException.class, () -> petService.createPet(shelterId, newPet));
        newPet.setSpecificNeeds("  ");
        assertThrows(IllegalOperationException.class, () -> petService.createPet(shelterId, newPet));
    }

    @Test
    void testDeletePetNotFound() {
        assertNotNull(assertThrows(EntityNotFoundException.class, () -> petService.deletePet(0L)));
    }
}
