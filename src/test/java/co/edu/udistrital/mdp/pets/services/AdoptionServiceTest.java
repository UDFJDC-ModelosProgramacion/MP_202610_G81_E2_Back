package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
import co.edu.udistrital.mdp.pets.repositories.AdoptionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdoptionServiceTest {

    @Mock
    private AdoptionRepository adoptionRepository;

    @InjectMocks
    private AdoptionService adoptionService;

    private PodamFactory factory = new PodamFactoryImpl();
    private AdoptionEntity adoption;

    @BeforeEach
    void setUp() {
        adoption = factory.manufacturePojo(AdoptionEntity.class);
        adoption.setTrialCohabitation(null);
        adoption.setPet(null);
    }

    @Test
    void testCreateAdoptionSuccess() {
        when(adoptionRepository.save(any(AdoptionEntity.class))).thenReturn(adoption);

        AdoptionEntity result = adoptionService.createAdoption(adoption);
        assertNotNull(result);
        assertEquals(adoption.getId(), result.getId());
    }

    @Test
    void testCreateAdoptionNull() {
        assertThrows(IllegalArgumentException.class, () -> adoptionService.createAdoption(null));
    }

    @Test
    void testSearchAdoptionSuccess() {
        when(adoptionRepository.findById(adoption.getId())).thenReturn(Optional.of(adoption));
        AdoptionEntity result = adoptionService.searchAdoption(adoption.getId());
        assertNotNull(result);
        assertEquals(adoption.getId(), result.getId());
    }

    @Test
    void testSearchAdoptionNotFound() {
        when(adoptionRepository.findById(adoption.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> adoptionService.searchAdoption(adoption.getId()));
    }

    @Test
    void testSearchAdoptions() {
        List<AdoptionEntity> list = Arrays.asList(adoption);
        when(adoptionRepository.findAll()).thenReturn(list);
        List<AdoptionEntity> result = adoptionService.searchAdoptions();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateAdoptionSuccess() {
        AdoptionEntity updatedAdoption = new AdoptionEntity();
        updatedAdoption.setOfficialDate(adoption.getOfficialDate());
        updatedAdoption.setContractSigned(!adoption.getContractSigned());

        when(adoptionRepository.findById(adoption.getId())).thenReturn(Optional.of(adoption));
        when(adoptionRepository.save(any(AdoptionEntity.class))).thenReturn(updatedAdoption);

        AdoptionEntity result = adoptionService.updateAdoption(adoption.getId(), updatedAdoption);
        assertNotNull(result);
        assertEquals(updatedAdoption.getContractSigned(), result.getContractSigned());
    }

    @Test
    void testUpdateAdoptionNotFound() {
        when(adoptionRepository.findById(adoption.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> adoptionService.updateAdoption(adoption.getId(), adoption));
    }

    @Test
    void testDeleteAdoptionSuccess() {
        when(adoptionRepository.findById(adoption.getId())).thenReturn(Optional.of(adoption));

        assertDoesNotThrow(() -> adoptionService.deleteAdoption(adoption.getId()));
        verify(adoptionRepository, times(1)).delete(adoption);
    }

    @Test
    void testDeleteAdoptionWithTrialCohabitation() {
        TrialCohabitationEntity trial = new TrialCohabitationEntity();
        adoption.setTrialCohabitation(trial);
        when(adoptionRepository.findById(adoption.getId())).thenReturn(Optional.of(adoption));

        assertThrows(IllegalArgumentException.class, () -> adoptionService.deleteAdoption(adoption.getId()));
        verify(adoptionRepository, never()).delete(any());
    }

    @Test
    void testDeleteAdoptionWithPet() {
        PetEntity pet = new PetEntity();
        adoption.setPet(pet);
        when(adoptionRepository.findById(adoption.getId())).thenReturn(Optional.of(adoption));

        assertThrows(IllegalArgumentException.class, () -> adoptionService.deleteAdoption(adoption.getId()));
        verify(adoptionRepository, never()).delete(any());
    }
}
