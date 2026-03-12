package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.ReturnCaseEntity;
import co.edu.udistrital.mdp.pets.repositories.ReturnCaseRepository;
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
public class ReturnCaseServiceTest {

    @Mock
    private ReturnCaseRepository returnCaseRepository;

    @InjectMocks
    private ReturnCaseService returnCaseService;

    private PodamFactory factory = new PodamFactoryImpl();
    private ReturnCaseEntity returnCase;

    @BeforeEach
    void setUp() {
        returnCase = factory.manufacturePojo(ReturnCaseEntity.class);
        returnCase.setAdoptionProcess(null);
    }

    @Test
    void testCreateReturnCaseSuccess() {
        when(returnCaseRepository.save(any(ReturnCaseEntity.class))).thenReturn(returnCase);

        ReturnCaseEntity result = returnCaseService.createReturnCase(returnCase);
        assertNotNull(result);
        assertEquals(returnCase.getId(), result.getId());
    }

    @Test
    void testCreateReturnCaseNull() {
        assertThrows(IllegalArgumentException.class, () -> returnCaseService.createReturnCase(null));
    }

    @Test
    void testSearchReturnCaseSuccess() {
        when(returnCaseRepository.findById(returnCase.getId())).thenReturn(Optional.of(returnCase));
        ReturnCaseEntity result = returnCaseService.searchReturnCase(returnCase.getId());
        assertNotNull(result);
        assertEquals(returnCase.getId(), result.getId());
    }

    @Test
    void testSearchReturnCaseNotFound() {
        when(returnCaseRepository.findById(returnCase.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> returnCaseService.searchReturnCase(returnCase.getId()));
    }

    @Test
    void testSearchReturnCases() {
        List<ReturnCaseEntity> list = Arrays.asList(returnCase);
        when(returnCaseRepository.findAll()).thenReturn(list);
        List<ReturnCaseEntity> result = returnCaseService.searchReturnCases();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateReturnCaseSuccess() {
        ReturnCaseEntity updatedReturnCase = new ReturnCaseEntity();
        updatedReturnCase.setReturnDate(returnCase.getReturnDate());
        updatedReturnCase.setReason("Updated Reason");
        updatedReturnCase.setDetails("Updated Details");

        when(returnCaseRepository.findById(returnCase.getId())).thenReturn(Optional.of(returnCase));
        when(returnCaseRepository.save(any(ReturnCaseEntity.class))).thenReturn(updatedReturnCase);

        ReturnCaseEntity result = returnCaseService.updateReturnCase(returnCase.getId(), updatedReturnCase);
        assertNotNull(result);
        assertEquals("Updated Reason", result.getReason());
        assertEquals("Updated Details", result.getDetails());
    }

    @Test
    void testUpdateReturnCaseNotFound() {
        when(returnCaseRepository.findById(returnCase.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> returnCaseService.updateReturnCase(returnCase.getId(), returnCase));
    }

    @Test
    void testDeleteReturnCaseSuccess() {
        when(returnCaseRepository.findById(returnCase.getId())).thenReturn(Optional.of(returnCase));

        assertDoesNotThrow(() -> returnCaseService.deleteReturnCase(returnCase.getId()));
        verify(returnCaseRepository, times(1)).delete(returnCase);
    }

    @Test
    void testDeleteReturnCaseWithAdoptionProcess() {
        co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity adoptionProcess = mock(co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity.class);
        returnCase.setAdoptionProcess(adoptionProcess);
        when(returnCaseRepository.findById(returnCase.getId())).thenReturn(Optional.of(returnCase));

        assertThrows(IllegalArgumentException.class, () -> returnCaseService.deleteReturnCase(returnCase.getId()));
        verify(returnCaseRepository, never()).delete(any());
    }
}
