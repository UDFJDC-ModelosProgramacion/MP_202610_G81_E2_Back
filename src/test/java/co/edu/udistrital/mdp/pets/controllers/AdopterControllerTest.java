package co.edu.udistrital.mdp.pets.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.dto.AdopterDTO;
import co.edu.udistrital.mdp.pets.dto.AdopterDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

@SpringBootTest
@Transactional
class AdopterControllerTest {

    @Autowired
    private AdopterController adopterController;

    private AdopterDTO adopterDTO;

    @SuppressWarnings("java:S1144")
    void initializeTestData() {
        adopterDTO = new AdopterDTO();
        adopterDTO.setName("Test Adopter");
        adopterDTO.setEmail("adopter@test.com");
        adopterDTO.setPassword("password123");
        adopterDTO.setPhoneNumber("0987654321");
        adopterDTO.setAddress("123 Main St");
        adopterDTO.setCity("Test City");
        adopterDTO.setHousingType("House");
        adopterDTO.setStatus("Active");
    }

    @Test
    void testCreateAdopter() throws IllegalOperationException {
        initializeTestData();
        AdopterDetailDTO createdAdopter = adopterController.create(adopterDTO);

        assertNotNull(createdAdopter);
        assertNotNull(createdAdopter.getId());
        assertEquals(adopterDTO.getName(), createdAdopter.getName());
        assertEquals(adopterDTO.getAddress(), createdAdopter.getAddress());
    }

    @Test
    void testFindAll() throws IllegalOperationException {
        initializeTestData();
        adopterController.create(adopterDTO);

        List<AdopterDTO> adopters = adopterController.findAll(null, null, null);
        assertNotNull(adopters);
        assertFalse(adopters.isEmpty());
    }

    @Test
    void testFindOne() throws IllegalOperationException, EntityNotFoundException {
        initializeTestData();
        AdopterDetailDTO createdAdopter = adopterController.create(adopterDTO);

        AdopterDetailDTO foundAdopter = adopterController.findOne(createdAdopter.getId());
        assertNotNull(foundAdopter);
        assertEquals(createdAdopter.getId(), foundAdopter.getId());
        assertEquals("Test Adopter", foundAdopter.getName());
    }

    @Test
    void testUpdate() throws IllegalOperationException, EntityNotFoundException {
        initializeTestData();
        AdopterDetailDTO createdAdopter = adopterController.create(adopterDTO);

        adopterDTO.setCity("Updated City");
        AdopterDetailDTO updatedAdopter = adopterController.update(createdAdopter.getId(), adopterDTO);

        assertNotNull(updatedAdopter);
        assertEquals("Updated City", updatedAdopter.getCity());
    }

    @Test
    void testDelete() throws IllegalOperationException, EntityNotFoundException {
        initializeTestData();
        AdopterDetailDTO createdAdopter = adopterController.create(adopterDTO);

        adopterController.delete(createdAdopter.getId());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            adopterController.findOne(createdAdopter.getId());
        });
        assertNotNull(exception);
    }
}
