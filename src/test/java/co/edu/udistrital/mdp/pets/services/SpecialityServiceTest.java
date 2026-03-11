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

    @Test
    void testSearchSpecialitySuccess() throws EntityNotFoundException {
        when(specialityRepository.findById(speciality.getId())).thenReturn(Optional.of(speciality));
        VetSpecialityEntity result = specialityService.searchSpeciality(speciality.getId());
        assertNotNull(result);
        assertEquals(speciality.getId(), result.getId());
    }

    @Test
    void testSearchSpecialityNotFound() {
        when(specialityRepository.findById(speciality.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> specialityService.searchSpeciality(speciality.getId()));
    }

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

    @Test
    void testDeleteSpecialitySuccess() throws EntityNotFoundException, IllegalOperationException {
        when(specialityRepository.findById(speciality.getId())).thenReturn(Optional.of(speciality));
        
        assertDoesNotThrow(() -> specialityService.deleteSpeciality(speciality.getId()));
        verify(specialityRepository, times(1)).deleteById(speciality.getId());
    }

    @Test
    void testDeleteSpecialityWithVeterinarians() {
        speciality.getVeterinarians().add(new VeterinarianEntity());
        when(specialityRepository.findById(speciality.getId())).thenReturn(Optional.of(speciality));

        assertThrows(IllegalOperationException.class, () -> specialityService.deleteSpeciality(speciality.getId()));
        verify(specialityRepository, never()).deleteById(anyLong());
    }
}
