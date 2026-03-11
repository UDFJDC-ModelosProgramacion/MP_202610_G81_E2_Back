package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterMediaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ShelterMediaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ShelterMediaServiceTest {

    @Mock
    private ShelterMediaRepository shelterMediaRepository;

    @InjectMocks
    private ShelterMediaService shelterMediaService;

    private PodamFactory factory = new PodamFactoryImpl();
    private ShelterMediaEntity mediaEntity;
    private ShelterEntity shelter;

    @BeforeEach
    void setUp() {
        shelter = factory.manufacturePojo(ShelterEntity.class);
        shelter.setId(1L);

        mediaEntity = factory.manufacturePojo(ShelterMediaEntity.class);
        mediaEntity.setId(10L);
        mediaEntity.setMediaUrl("image.jpg");
        mediaEntity.setMediaType("Foto de Perfil");
        mediaEntity.setShelter(shelter);
    }

    @Test
    void testCreateShelterMediaSuccess() throws IllegalOperationException {
        when(shelterMediaRepository.save(any(ShelterMediaEntity.class))).thenReturn(mediaEntity);

        ShelterMediaEntity result = shelterMediaService.createShelterMedia(mediaEntity, 1024); // 1KB
        assertNotNull(result);
        assertEquals("image.jpg", result.getMediaUrl());
    }

    @Test
    void testCreateShelterMediaInvalidFormat() {
        mediaEntity.setMediaUrl("document.pdf");
        assertThrows(IllegalOperationException.class, () -> shelterMediaService.createShelterMedia(mediaEntity, 1024));
    }

    @Test
    void testCreateShelterMediaExceedsSize() {
        long largeSize = 6 * 1024 * 1024; // 6MB
        assertThrows(IllegalOperationException.class, () -> shelterMediaService.createShelterMedia(mediaEntity, largeSize));
    }

    @Test
    void testCreateShelterMediaNoUrl() {
        mediaEntity.setMediaUrl("");
        assertThrows(IllegalOperationException.class, () -> shelterMediaService.createShelterMedia(mediaEntity, 1024));
    }

    @Test
    void testSearchShelterMediaSuccess() throws EntityNotFoundException {
        when(shelterMediaRepository.findById(mediaEntity.getId())).thenReturn(Optional.of(mediaEntity));
        ShelterMediaEntity result = shelterMediaService.searchShelterMedia(mediaEntity.getId());
        assertNotNull(result);
        assertEquals(mediaEntity.getId(), result.getId());
    }

    @Test
    void testSearchShelterMediaNotFound() {
        when(shelterMediaRepository.findById(mediaEntity.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> shelterMediaService.searchShelterMedia(mediaEntity.getId()));
    }

    @Test
    void testUpdateShelterMediaSuccess() throws EntityNotFoundException {
        ShelterMediaEntity updatedMedia = new ShelterMediaEntity();
        updatedMedia.setDescription("Nueva descripción");

        when(shelterMediaRepository.findById(mediaEntity.getId())).thenReturn(Optional.of(mediaEntity));
        when(shelterMediaRepository.save(any(ShelterMediaEntity.class))).thenReturn(mediaEntity);

        ShelterMediaEntity result = shelterMediaService.updateShelterMedia(mediaEntity.getId(), updatedMedia);
        assertNotNull(result);
        assertEquals("Nueva descripción", mediaEntity.getDescription()); // existing gets updated
    }

    @Test
    void testDeleteShelterMediaSuccessNotProfilePhoto() throws EntityNotFoundException, IllegalOperationException {
        mediaEntity.setMediaType("Video Tour");
        when(shelterMediaRepository.findById(mediaEntity.getId())).thenReturn(Optional.of(mediaEntity));

        assertDoesNotThrow(() -> shelterMediaService.deleteShelterMedia(mediaEntity.getId()));
        verify(shelterMediaRepository, times(1)).deleteById(mediaEntity.getId());
    }

    @Test
    void testDeleteShelterMediaSuccessProfilePhotoWithBackup() throws EntityNotFoundException, IllegalOperationException {
        when(shelterMediaRepository.findById(mediaEntity.getId())).thenReturn(Optional.of(mediaEntity));
        when(shelterMediaRepository.countByShelterIdAndMediaType(shelter.getId(), "Foto de Perfil")).thenReturn(2);

        assertDoesNotThrow(() -> shelterMediaService.deleteShelterMedia(mediaEntity.getId()));
        verify(shelterMediaRepository, times(1)).deleteById(mediaEntity.getId());
    }

    @Test
    void testDeleteShelterMediaFailsProfilePhotoNoBackup() {
        when(shelterMediaRepository.findById(mediaEntity.getId())).thenReturn(Optional.of(mediaEntity));
        when(shelterMediaRepository.countByShelterIdAndMediaType(shelter.getId(), "Foto de Perfil")).thenReturn(1);

        assertThrows(IllegalOperationException.class, () -> shelterMediaService.deleteShelterMedia(mediaEntity.getId()));
        verify(shelterMediaRepository, never()).deleteById(anyLong());
    }
}
