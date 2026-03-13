package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.ReturnCaseEntity;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

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
    private List<AdoptionProcessEntity> adoptionProcessList = new ArrayList<>();

    @BeforeEach
    void setUp(){
        clearData();
        insertData();
    }

    private void insertData() {
        
        for (int i = 0; i < 3; i++){
            AdoptionProcessEntity adoption = factory.manufacturePojo(AdoptionProcessEntity.class);
            entityManager.persist(adoption);
            adoptionProcessList.add(adoption);
        }

        for (int i = 0; i < 3; i++){
            ReturnCaseEntity returnCase = factory.manufacturePojo(ReturnCaseEntity.class);
            returnCase.setAdoptionProcess(adoptionProcessList.get(0));
            entityManager.persist(returnCase);
            returnCaseList.add(returnCase);
        }
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ReturnCaseEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopntionEntity").executeUpdate();
    }

    @Test
    void createReturnCaseTest(){
        ReturnCaseEntity newEntity = factory.manufacturePojo(ReturnCaseEntity.class);
        newEntity.setAdoptionProcess(adoptionProcessList.get(0));

        ReturnCaseEntity result = returnCaseService.createReturnCase(newEntity);

        assertNotNull(result);

        ReturnCaseEntity entity = entityManager.find(ReturnCaseEntity.class, result.getId());

        assertEquals(newEntity.getReason(), entity.getReason());
        assertEquals(newEntity.getDetails(), entity.getDetails());
        assertEquals(newEntity.getReturnDate(), entity.getReturnDate());
    }

    @Test
    void searchReturnCaseTest() {

        ReturnCaseEntity entity = returnCaseList.get(0);
        ReturnCaseEntity result = returnCaseService.searchReturnCase(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void searchReturnCasesTest(){
        List<ReturnCaseEntity> list = returnCaseService.searchReturnCases();
        assertEquals(returnCaseList.size(), list.size());
    }

    @Test
    void updateReturnCaseTest(){
        ReturnCaseEntity entity = returnCaseList.get(0);

        ReturnCaseEntity newEntity = factory.manufacturePojo(ReturnCaseEntity.class);
        newEntity.setId(entity.getId());
        newEntity.setAdoptionProcess((entity.getAdoptionProcess()));

        returnCaseService.updateReturnCase(entity.getId(), newEntity);

        ReturnCaseEntity resp = entityManager.find(ReturnCaseEntity.class, entity.getId());

        assertEquals(newEntity.getReason(), resp.getReason());
        assertEquals(newEntity.getDetails(), resp.getDetails());
        assertEquals(newEntity.getReturnDate(), resp.getReturnDate());
    }

    @Test
    void deleteReturnCaseTest(){
        ReturnCaseEntity entity = returnCaseList.get(0);

        returnCaseService.deleteReturnCase(entity.getId());

        ReturnCaseEntity deleted = entityManager.find(ReturnCaseEntity.class, entity.getId());

        assertNull(deleted);
    }
}