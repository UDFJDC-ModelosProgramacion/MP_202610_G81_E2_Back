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

import co.edu.udistrital.mdp.pets.dto.VeterinarianDTO;
import co.edu.udistrital.mdp.pets.dto.VeterinarianDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

@SpringBootTest
@Transactional
class VeterinarianControllerTest {

    @Autowired
    private VeterinarianController veterinarianController;

    @SuppressWarnings("java:S1144")
    VeterinarianDTO initializeVeterinarianData() {
        VeterinarianDTO veterinarianDTO = new VeterinarianDTO();
        veterinarianDTO.setName("Vet Base User");
        veterinarianDTO.setEmail("vet.user@test.com");
        veterinarianDTO.setPassword("password123");
        veterinarianDTO.setPhoneNumber("3000000000");
        veterinarianDTO.setLicenseNumber("LIC-TEST-001");
        veterinarianDTO.setAvailabilitySchedule("L-V 8:00-16:00");

        return veterinarianDTO;
    }

    @Test
    void testCreateVeterinarian() throws IllegalOperationException, EntityNotFoundException {
        VeterinarianDTO veterinarianDTO = initializeVeterinarianData();

        VeterinarianDetailDTO createdVeterinarian = veterinarianController.create(veterinarianDTO);

        assertNotNull(createdVeterinarian);
        assertNotNull(createdVeterinarian.getId());
        assertEquals(veterinarianDTO.getLicenseNumber(), createdVeterinarian.getLicenseNumber());
    }

    @Test
    void testFindAll() throws IllegalOperationException, EntityNotFoundException {
        veterinarianController.create(initializeVeterinarianData());

        List<VeterinarianDTO> veterinarians = veterinarianController.findAll(null, null);
        assertNotNull(veterinarians);
        assertFalse(veterinarians.isEmpty());
    }

    @Test
    void testFindOne() throws IllegalOperationException, EntityNotFoundException {
        VeterinarianDetailDTO createdVeterinarian = veterinarianController.create(initializeVeterinarianData());

        VeterinarianDetailDTO foundVeterinarian = veterinarianController.findOne(createdVeterinarian.getId());

        assertNotNull(foundVeterinarian);
        assertEquals(createdVeterinarian.getId(), foundVeterinarian.getId());
        assertEquals("LIC-TEST-001", foundVeterinarian.getLicenseNumber());
    }

    @Test
    void testUpdate() throws IllegalOperationException, EntityNotFoundException {
        VeterinarianDetailDTO createdVeterinarian = veterinarianController.create(initializeVeterinarianData());

        VeterinarianDTO updateDTO = new VeterinarianDTO();
        updateDTO.setId(createdVeterinarian.getId());
        updateDTO.setName("Vet Updated");
        updateDTO.setEmail("vet.user@test.com");
        updateDTO.setPhoneNumber("3111111111");
        updateDTO.setLicenseNumber("LIC-TEST-001");
        updateDTO.setAvailabilitySchedule("L-S 9:00-17:00");

        VeterinarianDetailDTO updatedVeterinarian = veterinarianController.update(createdVeterinarian.getId(), updateDTO);

        assertNotNull(updatedVeterinarian);
        assertEquals("Vet Updated", updatedVeterinarian.getName());
    }

    @Test
    void testDelete() throws IllegalOperationException, EntityNotFoundException {
        VeterinarianDetailDTO createdVeterinarian = veterinarianController.create(initializeVeterinarianData());

        veterinarianController.delete(createdVeterinarian.getId());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            veterinarianController.findOne(createdVeterinarian.getId());
        });
        assertNotNull(exception);
    }
}
