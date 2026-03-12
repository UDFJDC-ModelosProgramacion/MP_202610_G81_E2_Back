package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.PetRepository;
import co.edu.udistrital.mdp.pets.repositories.ShelterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShelterServiceTest {

    @Mock
    private ShelterRepository shelterRepository;

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private ShelterService shelterService;

    private PodamFactory factory = new PodamFactoryImpl();
    private ShelterEntity shelter;

    @BeforeEach
    void setUp() {
        shelter = factory.manufacturePojo(ShelterEntity.class);
        shelter.setNit("123456789");
        shelter.setShelterName("Shelter Test");
        shelter.setPhoneNumber("1234567");
        shelter.setAddress("Test Address");
        shelter.setStatus("Activo");
    }

    @SuppressWarnings("null")
    @Test
    void testCreateShelterSuccess() throws IllegalOperationException {
        when(shelterRepository.findByNit(shelter.getNit())).thenReturn(Optional.empty());
        when(shelterRepository.findByShelterName(shelter.getShelterName())).thenReturn(Collections.emptyList());
        when(shelterRepository.save(any(ShelterEntity.class))).thenReturn(shelter);

        ShelterEntity result = shelterService.createShelter(shelter);
        assertNotNull(result);
        assertEquals(shelter.getNit(), result.getNit());
        assertEquals("Activo", result.getStatus());
    }

    @Test
    void testCreateShelterMissingNit() {
        shelter.setNit(null);
        assertThrows(IllegalOperationException.class, () -> shelterService.createShelter(shelter));
    }

    @Test
    void testCreateShelterDuplicateNit() {
        when(shelterRepository.findByNit(shelter.getNit())).thenReturn(Optional.of(shelter));
        assertThrows(IllegalOperationException.class, () -> shelterService.createShelter(shelter));
    }

    @SuppressWarnings("null")
    @Test
    void testSearchShelterSuccess() throws EntityNotFoundException {
        when(shelterRepository.findById(shelter.getId())).thenReturn(Optional.of(shelter));
        ShelterEntity result = shelterService.searchShelter(shelter.getId());
        assertNotNull(result);
        assertEquals(shelter.getId(), result.getId());
    }

    @SuppressWarnings("null")
    @Test
    void testSearchShelterNotFound() {
        when(shelterRepository.findById(shelter.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> shelterService.searchShelter(shelter.getId()));
    }

    @Test
    void testSearchSheltersByNameNotFound() {
        when(shelterRepository.findByShelterName("Unknown")).thenReturn(Collections.emptyList());
        List<ShelterEntity> result = shelterService.searchSheltersByName("Unknown");
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @SuppressWarnings("null")
    @Test
    void testUpdateShelterSuccess() throws EntityNotFoundException, IllegalOperationException {
        ShelterEntity updatedShelter = new ShelterEntity();
        updatedShelter.setNit(shelter.getNit());
        updatedShelter.setStatus("Activo");
        updatedShelter.setShelterName("New Name");

        when(shelterRepository.findById(shelter.getId())).thenReturn(Optional.of(shelter));
        when(shelterRepository.findByShelterName("New Name")).thenReturn(Collections.emptyList());
        when(shelterRepository.save(any(ShelterEntity.class))).thenReturn(updatedShelter);

        ShelterEntity result = shelterService.updateShelter(shelter.getId(), updatedShelter);
        assertNotNull(result);
        assertEquals("New Name", result.getShelterName());
    }

    @SuppressWarnings("null")
    @Test
    void testUpdateShelterChangeNit() {
        ShelterEntity updatedShelter = new ShelterEntity();
        updatedShelter.setNit("987654321"); // Changed NIT

        when(shelterRepository.findById(shelter.getId())).thenReturn(Optional.of(shelter));

        assertThrows(IllegalOperationException.class,
                () -> shelterService.updateShelter(shelter.getId(), updatedShelter));
    }

    @SuppressWarnings("null")
    @Test
    void testUpdateShelterNotActive() {
        shelter.setStatus("Inactivo");
        ShelterEntity updatedShelter = new ShelterEntity();
        updatedShelter.setNit(shelter.getNit());

        when(shelterRepository.findById(shelter.getId())).thenReturn(Optional.of(shelter));

        assertThrows(IllegalOperationException.class,
                () -> shelterService.updateShelter(shelter.getId(), updatedShelter));
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteShelterSuccess() throws EntityNotFoundException, IllegalOperationException {
        when(shelterRepository.findById(shelter.getId())).thenReturn(Optional.of(shelter));
        when(petRepository.findByShelterId(shelter.getId())).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> shelterService.deleteShelter(shelter.getId()));
        verify(shelterRepository, times(1)).deleteById(shelter.getId());
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteShelterWithPets() {
        PetEntity pet = factory.manufacturePojo(PetEntity.class);
        when(shelterRepository.findById(shelter.getId())).thenReturn(Optional.of(shelter));
        when(petRepository.findByShelterId(shelter.getId())).thenReturn(Arrays.asList(pet));

        assertThrows(IllegalOperationException.class, () -> shelterService.deleteShelter(shelter.getId()));
        verify(shelterRepository, never()).deleteById(anyLong());
    }

    @Test
    void testSearchShelters() {
        when(shelterRepository.findAll()).thenReturn(Arrays.asList(shelter));
        List<ShelterEntity> list = shelterService.searchShelters();
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    void testSearchSheltersByNameSuccess() {
        when(shelterRepository.findByShelterName("Shelter Test")).thenReturn(Arrays.asList(shelter));
        List<ShelterEntity> result = shelterService.searchSheltersByName("Shelter Test");
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testCreateShelterMissingName() {
        shelter.setShelterName(null);
        assertThrows(IllegalOperationException.class, () -> shelterService.createShelter(shelter));
    }

    @Test
    void testCreateShelterMissingPhoneNumber() {
        shelter.setPhoneNumber(null);
        assertThrows(IllegalOperationException.class, () -> shelterService.createShelter(shelter));
    }

    @Test
    void testCreateShelterMissingAddress() {
        shelter.setAddress("   ");
        assertThrows(IllegalOperationException.class, () -> shelterService.createShelter(shelter));
    }

    @Test
    void testUpdateShelterDuplicateName() {
        ShelterEntity updatedShelter = new ShelterEntity();
        updatedShelter.setNit(shelter.getNit());
        updatedShelter.setStatus("Activo");
        updatedShelter.setShelterName("Another Name");

        ShelterEntity another = new ShelterEntity();
        another.setId(99L);
        another.setShelterName("Another Name");

        when(shelterRepository.findById(shelter.getId())).thenReturn(Optional.of(shelter));
        when(shelterRepository.findByShelterName("Another Name")).thenReturn(Arrays.asList(another));

        assertThrows(IllegalOperationException.class,
                () -> shelterService.updateShelter(shelter.getId(), updatedShelter));
    }
}
