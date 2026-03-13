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

import co.edu.udistrital.mdp.pets.entities.AdoptionEntity;
import co.edu.udistrital.mdp.pets.entities.PetEntity;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

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
    private List<PetEntity> petList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from AdoptionEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from PetEntity").executeUpdate();
    }

    private void insertData() {

        for (int i = 0; i < 3; i++) {
            PetEntity pet = factory.manufacturePojo(PetEntity.class);
            entityManager.persist(pet);
            petList.add(pet);
        }

        for (int i = 0; i < 3; i++) {
            AdoptionEntity adoption = factory.manufacturePojo(AdoptionEntity.class);
            adoption.setPet(petList.get(0));
            entityManager.persist(adoption);
            adoptionList.add(adoption);
        }
    }

    @Test
    void createAdoptionTest() {

        AdoptionEntity newEntity = factory.manufacturePojo(AdoptionEntity.class);
        newEntity.setPet(petList.get(0));

        AdoptionEntity result = adoptionService.createAdoption(newEntity);

        assertNotNull(result);

        AdoptionEntity entity = entityManager.find(AdoptionEntity.class, result.getId());

        assertEquals(newEntity.getOfficialDate(), entity.getOfficialDate());
        assertEquals(newEntity.getContractSigned(), entity.getContractSigned());
    }

    @Test
    void searchAdoptionTest(){

        AdoptionEntity entity = adoptionList.get(0);

        AdoptionEntity result = adoptionService.searchAdoption(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void searchAdoptionsTest(){

        List<AdoptionEntity> list = adoptionService.searchAdoptions();

        assertEquals(adoptionList.size(), list.size());
    }

    @Test
    void updateAdoptionTest(){

        AdoptionEntity entity = adoptionList.get(0);

        AdoptionEntity newEntity = factory.manufacturePojo(AdoptionEntity.class);
        newEntity.setId(entity.getId());
        newEntity.setPet(entity.getPet());

        adoptionService.updateAdoption(entity.getId(), newEntity);

        AdoptionEntity resp = entityManager.find(AdoptionEntity.class, entity.getId());

        assertEquals(newEntity.getOfficialDate(), resp.getOfficialDate());
        assertEquals(newEntity.getContractSigned(), resp.getContractSigned());
    }

    @Test
    void deleteAdoptionTest(){

        AdoptionEntity entity = adoptionList.get(0);

        adoptionService.deleteAdoption(entity.getId());

        AdoptionEntity deleted = entityManager.find(AdoptionEntity.class, entity.getId());

        assertNull(deleted);
    }
}