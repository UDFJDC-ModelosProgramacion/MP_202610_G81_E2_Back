package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.VetSpecialityEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
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
@Import(SpecialityService.class)
class SpecialityServiceTest {

    @Autowired
    private SpecialityService specialityService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<VetSpecialityEntity> specialityList = new ArrayList<>();
    private List<VeterinarianEntity> veterinarianList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from VetSpecialityEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from VeterinarianEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            VetSpecialityEntity specialityEntity = factory.manufacturePojo(VetSpecialityEntity.class);
            specialityEntity.setName("Especialidad-" + i + "-" + System.nanoTime());
            specialityEntity.setVeterinarians(new ArrayList<>());
            entityManager.persist(specialityEntity);
            specialityList.add(specialityEntity);
        }

        VeterinarianEntity veterinarianEntity = factory.manufacturePojo(VeterinarianEntity.class);
        veterinarianEntity.setLicenseNumber("LIC-" + System.nanoTime());
        veterinarianEntity.setSpecialities(new ArrayList<>());
        veterinarianEntity.setMedicalHistories(new ArrayList<>());
        veterinarianEntity.setMedicalEvents(new ArrayList<>());
        entityManager.persist(veterinarianEntity);
        veterinarianList.add(veterinarianEntity);

        veterinarianEntity.getSpecialities().add(specialityList.get(0));
        specialityList.get(0).getVeterinarians().add(veterinarianEntity);
    }

    @Test
    void testCreateSpeciality() {
        VetSpecialityEntity newEntity = factory.manufacturePojo(VetSpecialityEntity.class);
        newEntity.setName("Nueva Especialidad " + System.nanoTime());
        newEntity.setVeterinarians(new ArrayList<>());

        VetSpecialityEntity result = specialityService.createSpeciality(newEntity);
        assertNotNull(result);

        VetSpecialityEntity entity = entityManager.find(VetSpecialityEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getName(), entity.getName());
        assertEquals(newEntity.getDescription(), entity.getDescription());
    }

    @Test
    void testCreateSpecialityMissingName() {
        assertThrows(IllegalOperationException.class, () -> {
            VetSpecialityEntity newEntity = factory.manufacturePojo(VetSpecialityEntity.class);
            newEntity.setName(null);
            specialityService.createSpeciality(newEntity);
        });
    }

    @Test
    void testCreateSpecialityEmptyName() {
        assertThrows(IllegalOperationException.class, () -> {
            VetSpecialityEntity newEntity = factory.manufacturePojo(VetSpecialityEntity.class);
            newEntity.setName("");
            specialityService.createSpeciality(newEntity);
        });
    }

    @Test
    void testCreateSpecialityDuplicateName() {
        assertThrows(IllegalOperationException.class, () -> {
            VetSpecialityEntity newEntity = factory.manufacturePojo(VetSpecialityEntity.class);
            newEntity.setName(specialityList.get(0).getName());
            specialityService.createSpeciality(newEntity);
        });
    }

    @Test
    void testSearchSpeciality() {
        VetSpecialityEntity entity = specialityList.get(0);
        VetSpecialityEntity resultEntity = specialityService.searchSpeciality(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getName(), resultEntity.getName());
        assertEquals(entity.getDescription(), resultEntity.getDescription());
    }

    @Test
    void testSearchSpecialityNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            specialityService.searchSpeciality(0L);
        });
    }

    @Test
    void testSearchSpecialitiesAll() {
        List<VetSpecialityEntity> result = specialityService.searchSpecialities(null);
        assertNotNull(result);
        assertEquals(specialityList.size(), result.size());
    }

    @Test
    void testSearchSpecialitiesEmpty() {
        List<VetSpecialityEntity> result = specialityService.searchSpecialities("");
        assertNotNull(result);
        assertEquals(specialityList.size(), result.size());
    }

    @Test
    void testSearchSpecialitiesByName() {
        String searchTerm = specialityList.get(0).getName().substring(0, 10);
        List<VetSpecialityEntity> result = specialityService.searchSpecialities(searchTerm);
        assertNotNull(result);
        assertFalse(result.isEmpty());
    }

    @Test
    void testUpdateSpeciality() {
        VetSpecialityEntity entity = specialityList.get(0);
        VetSpecialityEntity updated = new VetSpecialityEntity();
        updated.setName("Nombre Actualizado " + System.nanoTime());

        VetSpecialityEntity result = specialityService.updateSpeciality(entity.getId(), updated);
        assertNotNull(result);

        VetSpecialityEntity resp = entityManager.find(VetSpecialityEntity.class, entity.getId());
        assertEquals(updated.getName(), resp.getName());
    }

    @Test
    void testUpdateSpecialityNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            VetSpecialityEntity updated = new VetSpecialityEntity();
            updated.setName("Nombre Nuevo");
            specialityService.updateSpeciality(0L, updated);
        });
    }

    @Test
    void testUpdateSpecialityDuplicateName() {
        assertThrows(IllegalOperationException.class, () -> {
            VetSpecialityEntity entity = specialityList.get(0);
            VetSpecialityEntity updated = new VetSpecialityEntity();
            updated.setName(specialityList.get(1).getName());
            specialityService.updateSpeciality(entity.getId(), updated);
        });
    }

    @Test
    void testUpdateSpecialityDescriptionOnly() {
        VetSpecialityEntity entity = specialityList.get(0);
        VetSpecialityEntity updated = new VetSpecialityEntity();
        updated.setDescription("Nueva descripción");

        VetSpecialityEntity result = specialityService.updateSpeciality(entity.getId(), updated);
        assertNotNull(result);

        VetSpecialityEntity resp = entityManager.find(VetSpecialityEntity.class, entity.getId());
        assertEquals("Nueva descripción", resp.getDescription());
    }

    @Test
    void testDeleteSpeciality() {
        VetSpecialityEntity entity = specialityList.get(1);
        specialityService.deleteSpeciality(entity.getId());
        VetSpecialityEntity deleted = entityManager.find(VetSpecialityEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteSpecialityNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            specialityService.deleteSpeciality(0L);
        });
    }

    @Test
    void testDeleteSpecialityWithVeterinarians() {
        assertThrows(IllegalOperationException.class, () -> {
            VetSpecialityEntity entity = specialityList.get(0);
            specialityService.deleteSpeciality(entity.getId());
        });
    }
}
