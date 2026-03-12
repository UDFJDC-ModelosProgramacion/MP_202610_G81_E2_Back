package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;
import co.edu.udistrital.mdp.pets.entities.MedicalHistoryEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.UserRepository;
import co.edu.udistrital.mdp.pets.repositories.VeterinarianRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VeterinarianServiceTest {

    @Mock
    private VeterinarianRepository veterinarianRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private VeterinarianService veterinarianService;

    private PodamFactory factory = new PodamFactoryImpl();
    private VeterinarianEntity veterinarian;

    @BeforeEach
    void setUp() {
        veterinarian = factory.manufacturePojo(VeterinarianEntity.class);
        veterinarian.setId(1L);
        veterinarian.setLicenseNumber("LIC-12345");
        veterinarian.setMedicalHistories(new ArrayList<>());
        veterinarian.setMedicalEvents(new ArrayList<>());
    }

    @SuppressWarnings("null")
    @Test
    void testCreateVeterinarianSuccess() throws IllegalOperationException, EntityNotFoundException {
        when(userRepository.existsById(veterinarian.getId())).thenReturn(true);
        when(veterinarianRepository.save(any(VeterinarianEntity.class))).thenReturn(veterinarian);

        VeterinarianEntity result = veterinarianService.createVeterinarian(veterinarian);
        assertNotNull(result);
        assertEquals(veterinarian.getLicenseNumber(), result.getLicenseNumber());
    }

    @Test
    void testCreateVeterinarianNoLicense() {
        veterinarian.setLicenseNumber(null);
        assertThrows(IllegalOperationException.class, () -> veterinarianService.createVeterinarian(veterinarian));
    }

    @Test
    void testCreateVeterinarianNoId() {
        veterinarian.setId(null);
        assertThrows(IllegalOperationException.class, () -> veterinarianService.createVeterinarian(veterinarian));
    }

    @SuppressWarnings("null")
    @Test
    void testCreateVeterinarianUserNotFound() {
        when(userRepository.existsById(veterinarian.getId())).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> veterinarianService.createVeterinarian(veterinarian));
    }

    @SuppressWarnings("null")
    @Test
    void testSearchVeterinarianSuccess() throws EntityNotFoundException {
        when(veterinarianRepository.findById(veterinarian.getId())).thenReturn(Optional.of(veterinarian));
        VeterinarianEntity result = veterinarianService.searchVeterinarian(veterinarian.getId());
        assertNotNull(result);
        assertEquals(veterinarian.getId(), result.getId());
    }

    @SuppressWarnings("null")
    @Test
    void testSearchVeterinarianNotFound() {
        when(veterinarianRepository.findById(veterinarian.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> veterinarianService.searchVeterinarian(veterinarian.getId()));
    }

    @SuppressWarnings("null")
    @Test
    void testSearchVeterinarians() {
        when(veterinarianRepository.findAll()).thenReturn(Collections.singletonList(veterinarian));
        List<VeterinarianEntity> list = veterinarianService.searchVeterinarians();
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    void testSearchVeterinariansBySpeciality() {
        when(veterinarianRepository.findBySpecialitiesId(10L)).thenReturn(Collections.singletonList(veterinarian));
        List<VeterinarianEntity> list = veterinarianService.searchVeterinariansBySpeciality(10L);
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    void testSearchVeterinariansByShelter() {
        when(veterinarianRepository.findByShelterId(5L)).thenReturn(Collections.singletonList(veterinarian));
        List<VeterinarianEntity> list = veterinarianService.searchVeterinariansByShelter(5L);
        assertNotNull(list);
        assertEquals(1, list.size());
    }

    @Test
    void testUpdateVeterinarianSuccess() throws EntityNotFoundException, IllegalOperationException {
        VeterinarianEntity updatedVeterinarian = new VeterinarianEntity();
        updatedVeterinarian.setId(veterinarian.getId());
        updatedVeterinarian.setEmail("new@test.com");

        when(veterinarianRepository.findById(veterinarian.getId())).thenReturn(Optional.of(veterinarian));
        when(veterinarianRepository.save(any(VeterinarianEntity.class))).thenReturn(veterinarian);

        VeterinarianEntity result = veterinarianService.updateVeterinarian(veterinarian.getId(), updatedVeterinarian);
        assertNotNull(result);
    }

    @SuppressWarnings("null")
    @Test
    void testUpdateVeterinarianChangeIdFails() {
        VeterinarianEntity updatedVeterinarian = new VeterinarianEntity();
        updatedVeterinarian.setId(2L); // Different ID
        when(veterinarianRepository.findById(veterinarian.getId())).thenReturn(Optional.of(veterinarian));

        assertThrows(IllegalOperationException.class, () -> veterinarianService.updateVeterinarian(veterinarian.getId(), updatedVeterinarian));
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteVeterinarianSuccess() throws EntityNotFoundException, IllegalOperationException {
        when(veterinarianRepository.findById(veterinarian.getId())).thenReturn(Optional.of(veterinarian));
        
        assertDoesNotThrow(() -> veterinarianService.deleteVeterinarian(veterinarian.getId()));
        verify(veterinarianRepository, times(1)).deleteById(veterinarian.getId());
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteVeterinarianWithMedicalHistory() {
        veterinarian.getMedicalHistories().add(new MedicalHistoryEntity());
        when(veterinarianRepository.findById(veterinarian.getId())).thenReturn(Optional.of(veterinarian));

        assertThrows(IllegalOperationException.class, () -> veterinarianService.deleteVeterinarian(veterinarian.getId()));
        verify(veterinarianRepository, never()).deleteById(anyLong());
    }
    
    @SuppressWarnings("null")
    @Test
    void testDeleteVeterinarianWithMedicalEvents() {
        veterinarian.getMedicalEvents().add(new MedicalEventEntity());
        when(veterinarianRepository.findById(veterinarian.getId())).thenReturn(Optional.of(veterinarian));

        assertThrows(IllegalOperationException.class, () -> veterinarianService.deleteVeterinarian(veterinarian.getId()));
        verify(veterinarianRepository, never()).deleteById(anyLong());
    }
}
