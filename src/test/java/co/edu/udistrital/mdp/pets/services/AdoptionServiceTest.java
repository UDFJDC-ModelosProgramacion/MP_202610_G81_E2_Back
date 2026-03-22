package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import co.edu.udistrital.mdp.pets.entities.TrialCohabitationEntity;
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
@Import(AdoptionService.class)
class AdoptionServiceTest {

    @Autowired
    private AdoptionService adoptionService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<AdoptionEntity> adoptionList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            AdoptionEntity adoptionEntity = factory.manufacturePojo(AdoptionEntity.class);
            entityManager.persist(adoptionEntity);
            adoptionList.add(adoptionEntity);
        }
    }

    @Test
    void testCreateAdoption() {
        AdoptionEntity newEntity = factory.manufacturePojo(AdoptionEntity.class);

        AdoptionEntity result = adoptionService.createAdoption(newEntity);
        assertNotNull(result);

        AdoptionEntity entity = entityManager.find(AdoptionEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getOfficialDate(), entity.getOfficialDate());
        assertEquals(newEntity.getContractSigned(), entity.getContractSigned());
    }

    @Test
    void testCreateAdoptionNull() {
        assertThrows(IllegalOperationException.class, () -> adoptionService.createAdoption(null));
    }

    @Test
    void testSearchAdoption() {
        AdoptionEntity entity = adoptionList.get(0);
        AdoptionEntity resultEntity = adoptionService.searchAdoption(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getOfficialDate(), resultEntity.getOfficialDate());
        assertEquals(entity.getContractSigned(), resultEntity.getContractSigned());
    }

    @Test
    void testSearchAdoptionNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            adoptionService.searchAdoption(0L);
        });
    }

    @Test
    void testSearchAdoptions() {
        List<AdoptionEntity> list = adoptionService.searchAdoptions();
        assertEquals(adoptionList.size(), list.size());
        for (AdoptionEntity entity : list) {
            boolean found = false;
            for (AdoptionEntity storedEntity : adoptionList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testUpdateAdoption() {
        AdoptionEntity entity = adoptionList.get(0);
        AdoptionEntity pojoEntity = factory.manufacturePojo(AdoptionEntity.class);
        pojoEntity.setId(entity.getId());

        adoptionService.updateAdoption(entity.getId(), pojoEntity);

        AdoptionEntity resp = entityManager.find(AdoptionEntity.class, entity.getId());
        assertEquals(pojoEntity.getOfficialDate(), resp.getOfficialDate());
        assertEquals(pojoEntity.getContractSigned(), resp.getContractSigned());
    }

    @Test
    void testUpdateAdoptionNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            AdoptionEntity pojoEntity = factory.manufacturePojo(AdoptionEntity.class);
            pojoEntity.setId(0L);
            adoptionService.updateAdoption(0L, pojoEntity);
        });
    }

    @Test
    void testDeleteAdoption() {
        AdoptionEntity entity = adoptionList.get(1);
        adoptionService.deleteAdoption(entity.getId());
        AdoptionEntity deleted = entityManager.find(AdoptionEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteAdoptionNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            adoptionService.deleteAdoption(0L);
        });
    }

    @Test
    void testDeleteAdoptionWithTrialCohabitation() {
        assertThrows(IllegalOperationException.class, () -> {
            AdoptionEntity entity = adoptionList.get(0);
            TrialCohabitationEntity trial = factory.manufacturePojo(TrialCohabitationEntity.class);
            entityManager.persist(trial);
            entity.setTrialCohabitation(trial);
            entityManager.merge(entity);
            adoptionService.deleteAdoption(entity.getId());
        });
    }

    @Test
    void testDeleteAdoptionWithPet() {
        assertThrows(IllegalOperationException.class, () -> {
            AdoptionEntity entity = adoptionList.get(0);
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);
            entity.setPet(pet);
            entityManager.merge(entity);
            adoptionService.deleteAdoption(entity.getId());
        });
    }
}
