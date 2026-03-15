package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterMediaEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(ShelterMediaService.class)
class ShelterMediaServiceTest {

    @Autowired
    private ShelterMediaService shelterMediaService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<ShelterMediaEntity> mediaList = new ArrayList<>();
    private List<ShelterEntity> shelterList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ShelterMediaEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ShelterEntity shelterEntity = factory.manufacturePojo(ShelterEntity.class);
            entityManager.persist(shelterEntity);
            shelterList.add(shelterEntity);
        }

        for (int i = 0; i < 3; i++) {
            ShelterMediaEntity mediaEntity = factory.manufacturePojo(ShelterMediaEntity.class);
            mediaEntity.setMediaUrl("image" + i + ".jpg");
            mediaEntity.setMediaType("Foto de Galería");
            mediaEntity.setShelter(shelterList.get(0));
            entityManager.persist(mediaEntity);
            mediaList.add(mediaEntity);
        }
    }

    @Test
    void testCreateShelterMedia() throws IllegalOperationException {
        ShelterMediaEntity newEntity = factory.manufacturePojo(ShelterMediaEntity.class);
        newEntity.setMediaUrl("nueva_imagen.jpg");
        newEntity.setMediaType("Foto de Galería");
        newEntity.setShelter(shelterList.get(0));

        ShelterMediaEntity result = shelterMediaService.createShelterMedia(newEntity, 1024);
        assertNotNull(result);

        ShelterMediaEntity entity = entityManager.find(ShelterMediaEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getMediaUrl(), entity.getMediaUrl());
        assertEquals(newEntity.getMediaType(), entity.getMediaType());
    }

    @Test
    void testCreateShelterMediaInvalidFormat() {
        assertThrows(IllegalOperationException.class, () -> {
            ShelterMediaEntity newEntity = factory.manufacturePojo(ShelterMediaEntity.class);
            newEntity.setMediaUrl("document.pdf");
            newEntity.setShelter(shelterList.get(0));
            shelterMediaService.createShelterMedia(newEntity, 1024);
        });
    }

    @Test
    void testCreateShelterMediaExceedsSize() {
        assertThrows(IllegalOperationException.class, () -> {
            ShelterMediaEntity newEntity = factory.manufacturePojo(ShelterMediaEntity.class);
            newEntity.setMediaUrl("image.jpg");
            newEntity.setShelter(shelterList.get(0));
            long largeSize = 6 * 1024 * 1024; // 6MB
            shelterMediaService.createShelterMedia(newEntity, largeSize);
        });
    }

    @Test
    void testCreateShelterMediaNoUrl() {
        assertThrows(IllegalOperationException.class, () -> {
            ShelterMediaEntity newEntity = factory.manufacturePojo(ShelterMediaEntity.class);
            newEntity.setMediaUrl("");
            newEntity.setShelter(shelterList.get(0));
            shelterMediaService.createShelterMedia(newEntity, 1024);
        });
    }

    @Test
    void testCreateShelterMediaNullUrl() {
        assertThrows(IllegalOperationException.class, () -> {
            ShelterMediaEntity newEntity = factory.manufacturePojo(ShelterMediaEntity.class);
            newEntity.setMediaUrl(null);
            newEntity.setShelter(shelterList.get(0));
            shelterMediaService.createShelterMedia(newEntity, 1024);
        });
    }

    @Test
    void testSearchShelterMedia() throws EntityNotFoundException {
        ShelterMediaEntity entity = mediaList.get(0);
        ShelterMediaEntity resultEntity = shelterMediaService.searchShelterMedia(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getMediaUrl(), resultEntity.getMediaUrl());
        assertEquals(entity.getMediaType(), resultEntity.getMediaType());
    }

    @Test
    void testSearchShelterMediaNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            shelterMediaService.searchShelterMedia(0L);
        });
    }

    @Test
    void testSearchShelterMediasByShelterId() {
        List<ShelterMediaEntity> result = shelterMediaService.searchShelterMediasByShelterId(shelterList.get(0).getId());
        assertNotNull(result);
        assertEquals(3, result.size());
    }

    @Test
    void testUpdateShelterMedia() throws EntityNotFoundException {
        ShelterMediaEntity entity = mediaList.get(0);
        ShelterMediaEntity updatedMedia = new ShelterMediaEntity();
        updatedMedia.setDescription("Nueva descripción");

        ShelterMediaEntity result = shelterMediaService.updateShelterMedia(entity.getId(), updatedMedia);
        assertNotNull(result);

        ShelterMediaEntity resp = entityManager.find(ShelterMediaEntity.class, entity.getId());
        assertEquals("Nueva descripción", resp.getDescription());
    }

    @Test
    void testUpdateShelterMediaNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            ShelterMediaEntity updatedMedia = new ShelterMediaEntity();
            updatedMedia.setDescription("Nueva descripción");
            shelterMediaService.updateShelterMedia(0L, updatedMedia);
        });
    }

    @Test
    void testDeleteShelterMediaNotProfilePhoto() throws EntityNotFoundException, IllegalOperationException {
        ShelterMediaEntity entity = mediaList.get(0);
        entity.setMediaType("Video Tour");
        entityManager.merge(entity);

        shelterMediaService.deleteShelterMedia(entity.getId());
        ShelterMediaEntity deleted = entityManager.find(ShelterMediaEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteShelterMediaProfilePhotoWithBackup() throws EntityNotFoundException, IllegalOperationException {
        ShelterMediaEntity entity1 = mediaList.get(0);
        entity1.setMediaType("Foto de Perfil");
        entityManager.merge(entity1);

        ShelterMediaEntity entity2 = mediaList.get(1);
        entity2.setMediaType("Foto de Perfil");
        entityManager.merge(entity2);

        shelterMediaService.deleteShelterMedia(entity1.getId());
        ShelterMediaEntity deleted = entityManager.find(ShelterMediaEntity.class, entity1.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteShelterMediaProfilePhotoNoBackup() {
        assertThrows(IllegalOperationException.class, () -> {
            ShelterMediaEntity entity = mediaList.get(0);
            entity.setMediaType("Foto de Perfil");
            entityManager.merge(entity);
            shelterMediaService.deleteShelterMedia(entity.getId());
        });
    }

    @Test
    void testDeleteShelterMediaProfilePhotoNullShelter() {
        assertThrows(IllegalOperationException.class, () -> {
            ShelterMediaEntity entity = mediaList.get(0);
            entity.setMediaType("Foto de Perfil");
            entity.setShelter(null);
            entityManager.merge(entity);
            shelterMediaService.deleteShelterMedia(entity.getId());
        });
    }

    @Test
    void testDeleteShelterMediaNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            shelterMediaService.deleteShelterMedia(0L);
        });
    }
}
