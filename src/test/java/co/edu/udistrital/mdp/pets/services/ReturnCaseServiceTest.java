package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.ReturnCaseEntity;
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
@Import(ReturnCaseService.class)
class ReturnCaseServiceTest {

    @Autowired
    private ReturnCaseService returnCaseService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<ReturnCaseEntity> returnCaseList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ReturnCaseEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            ReturnCaseEntity returnCaseEntity = factory.manufacturePojo(ReturnCaseEntity.class);
            entityManager.persist(returnCaseEntity);
            returnCaseList.add(returnCaseEntity);
        }
    }

    @Test
    void testCreateReturnCase() throws EntityNotFoundException, IllegalOperationException {
        ReturnCaseEntity newEntity = factory.manufacturePojo(ReturnCaseEntity.class);

        ReturnCaseEntity result = returnCaseService.createReturnCase(newEntity);
        assertNotNull(result);

        ReturnCaseEntity entity = entityManager.find(ReturnCaseEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getReturnDate(), entity.getReturnDate());
        assertEquals(newEntity.getReason(), entity.getReason());
        assertEquals(newEntity.getDetails(), entity.getDetails());
    }

    @Test
    void testCreateReturnCaseNull() {
        assertThrows(IllegalOperationException.class, () -> returnCaseService.createReturnCase(null));
    }

    @Test
    void testSearchReturnCase() throws EntityNotFoundException {
        ReturnCaseEntity entity = returnCaseList.get(0);
        ReturnCaseEntity resultEntity = returnCaseService.searchReturnCase(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getReturnDate(), resultEntity.getReturnDate());
        assertEquals(entity.getReason(), resultEntity.getReason());
        assertEquals(entity.getDetails(), resultEntity.getDetails());
    }

    @Test
    void testSearchReturnCaseNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            returnCaseService.searchReturnCase(0L);
        });
    }

    @Test
    void testSearchReturnCases() {
        List<ReturnCaseEntity> list = returnCaseService.searchReturnCases();
        assertEquals(returnCaseList.size(), list.size());
        for (ReturnCaseEntity entity : list) {
            boolean found = false;
            for (ReturnCaseEntity storedEntity : returnCaseList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testUpdateReturnCase() throws EntityNotFoundException, IllegalOperationException {
        ReturnCaseEntity entity = returnCaseList.get(0);
        ReturnCaseEntity pojoEntity = factory.manufacturePojo(ReturnCaseEntity.class);
        pojoEntity.setId(entity.getId());

        returnCaseService.updateReturnCase(entity.getId(), pojoEntity);

        ReturnCaseEntity resp = entityManager.find(ReturnCaseEntity.class, entity.getId());
        assertEquals(pojoEntity.getReturnDate(), resp.getReturnDate());
        assertEquals(pojoEntity.getReason(), resp.getReason());
        assertEquals(pojoEntity.getDetails(), resp.getDetails());
    }

    @Test
    void testUpdateReturnCaseNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            ReturnCaseEntity pojoEntity = factory.manufacturePojo(ReturnCaseEntity.class);
            pojoEntity.setId(0L);
            returnCaseService.updateReturnCase(0L, pojoEntity);
        });
    }

    @Test
    void testDeleteReturnCase() throws EntityNotFoundException, IllegalOperationException {
        ReturnCaseEntity entity = returnCaseList.get(1);
        returnCaseService.deleteReturnCase(entity.getId());
        ReturnCaseEntity deleted = entityManager.find(ReturnCaseEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteReturnCaseNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            returnCaseService.deleteReturnCase(0L);
        });
    }

    @Test
    void testDeleteReturnCaseWithAdoptionProcess() {
        assertThrows(IllegalOperationException.class, () -> {
            ReturnCaseEntity entity = returnCaseList.get(0);
            TrialCohabitationEntity adoptionProcess = factory.manufacturePojo(TrialCohabitationEntity.class);
            entityManager.persist(adoptionProcess);
            entity.setAdoptionProcess(adoptionProcess);
            entityManager.merge(entity);
            returnCaseService.deleteReturnCase(entity.getId());
        });
    }
}
