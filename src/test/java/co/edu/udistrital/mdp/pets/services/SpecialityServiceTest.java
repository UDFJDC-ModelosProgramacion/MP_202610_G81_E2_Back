package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.VetSpecialityEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.VetSpecialityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpecialityServiceTest {

    @Mock
    private VetSpecialityRepository specialityRepository;

    @InjectMocks
    private SpecialityService specialityService;

    private PodamFactory factory = new PodamFactoryImpl();
    private VetSpecialityEntity speciality;

    @BeforeEach
    void setUp() {
        speciality = factory.manufacturePojo(VetSpecialityEntity.class);
        speciality.setId(1L);
        speciality.setName("Cardiología");
        speciality.setVeterinarians(new ArrayList<>());
    }

    @SuppressWarnings("null")
    @Test
    void testCreateSpecialitySuccess() throws IllegalOperationException {
        when(specialityRepository.findByNameIgnoreCase(speciality.getName().trim())).thenReturn(Optional.empty());
        when(specialityRepository.save(any(VetSpecialityEntity.class))).thenReturn(speciality);

        VetSpecialityEntity result = specialityService.createSpeciality(speciality);
        assertNotNull(result);
        assertEquals(speciality.getName(), result.getName());
    }

    @Test
    void testCreateSpecialityMissingName() {
        speciality.setName(null);
        assertThrows(IllegalOperationException.class, () -> specialityService.createSpeciality(speciality));
    }

    @Test
    void testCreateSpecialityDuplicateName() {
        when(specialityRepository.findByNameIgnoreCase(speciality.getName().trim())).thenReturn(Optional.of(speciality));
        assertThrows(IllegalOperationException.class, () -> specialityService.createSpeciality(speciality));
    }

    @SuppressWarnings("null")
    @Test
    void testSearchSpecialitySuccess() throws EntityNotFoundException {
        when(specialityRepository.findById(speciality.getId())).thenReturn(Optional.of(speciality));
        VetSpecialityEntity result = specialityService.searchSpeciality(speciality.getId());
        assertNotNull(result);
        assertEquals(speciality.getId(), result.getId());
    }

    @SuppressWarnings("null")
    @Test
    void testSearchSpecialityNotFound() {
        when(specialityRepository.findById(speciality.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> specialityService.searchSpeciality(speciality.getId()));
    }

    @SuppressWarnings("null")
    @Test
    void testUpdateSpecialitySuccess() throws EntityNotFoundException, IllegalOperationException {
        VetSpecialityEntity updated = new VetSpecialityEntity();
        updated.setName("Neurología");

        when(specialityRepository.findById(speciality.getId())).thenReturn(Optional.of(speciality));
        when(specialityRepository.findByNameIgnoreCase(updated.getName())).thenReturn(Optional.empty());
        when(specialityRepository.save(any(VetSpecialityEntity.class))).thenReturn(speciality);

        VetSpecialityEntity result = specialityService.updateSpeciality(speciality.getId(), updated);
        assertNotNull(result);
    }
    
    @SuppressWarnings("null")
    @Test
    void testUpdateSpecialityDuplicateName() {
        VetSpecialityEntity updated = new VetSpecialityEntity();
        updated.setName("Neurología");

        VetSpecialityEntity existingDuplicate = new VetSpecialityEntity();
        existingDuplicate.setId(2L);
        existingDuplicate.setName("Neurología");

        when(specialityRepository.findById(speciality.getId())).thenReturn(Optional.of(speciality));
        when(specialityRepository.findByNameIgnoreCase(updated.getName())).thenReturn(Optional.of(existingDuplicate));

        assertThrows(IllegalOperationException.class, () -> specialityService.updateSpeciality(speciality.getId(), updated));
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteSpecialitySuccess() throws EntityNotFoundException, IllegalOperationException {
        when(specialityRepository.findById(speciality.getId())).thenReturn(Optional.of(speciality));
        
        assertDoesNotThrow(() -> specialityService.deleteSpeciality(speciality.getId()));
        verify(specialityRepository, times(1)).deleteById(speciality.getId());
    }

    @SuppressWarnings("null")
    @Test
    void testDeleteSpecialityWithVeterinarians() {
        speciality.getVeterinarians().add(new VeterinarianEntity());
        when(specialityRepository.findById(speciality.getId())).thenReturn(Optional.of(speciality));

        assertThrows(IllegalOperationException.class, () -> specialityService.deleteSpeciality(speciality.getId()));
        verify(specialityRepository, never()).deleteById(anyLong());
    }

    @Test
    void testSearchSpecialitiesNullOrEmpty() {
        when(specialityRepository.findAll()).thenReturn(Arrays.asList(speciality));
        List<VetSpecialityEntity> resultNull = specialityService.searchSpecialities(null);
        List<VetSpecialityEntity> resultEmpty = specialityService.searchSpecialities("");
        
        assertNotNull(resultNull);
        assertNotNull(resultEmpty);
        assertEquals(1, resultNull.size());
        assertEquals(1, resultEmpty.size());
    }

    @Test
    void testSearchSpecialitiesByName() {
        when(specialityRepository.findByNameContainingIgnoreCase("Cardio")).thenReturn(Arrays.asList(speciality));
        List<VetSpecialityEntity> result = specialityService.searchSpecialities("Cardio");
        
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateSpecialityDescription() throws EntityNotFoundException, IllegalOperationException {
        VetSpecialityEntity updated = new VetSpecialityEntity();
        updated.setDescription("New description"); // Name is null
        
        when(specialityRepository.findById(speciality.getId())).thenReturn(Optional.of(speciality));
        when(specialityRepository.save(any(VetSpecialityEntity.class))).thenReturn(speciality);

        VetSpecialityEntity result = specialityService.updateSpeciality(speciality.getId(), updated);
        assertNotNull(result);
        verify(specialityRepository, times(1)).save(speciality);
    }
}
