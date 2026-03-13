package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(AdopterService.class)
class AdopterServiceTest {

    @Autowired
    private AdopterService adopterService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<AdopterEntity> adopterList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from MessageEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from NotificationEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            AdopterEntity adopterEntity = factory.manufacturePojo(AdopterEntity.class);
            adopterEntity.setEmail("adopter" + i + "@mail.com");
            adopterEntity.setName("Adopter " + i);
            adopterEntity.setPassword("Password123");
            adopterEntity.setPhone("30000000" + i);
            entityManager.persist(adopterEntity);
            adopterList.add(adopterEntity);
        }
    }

    @Test
    void testCreateAdopter() throws Exception {
        AdopterEntity newAdopter = factory.manufacturePojo(AdopterEntity.class);
        newAdopter.setEmail("newadopter@mail.com");
        newAdopter.setName("New Adopter");
        newAdopter.setPassword("Secure123");
        newAdopter.setPhone("3001111111");

        AdopterEntity result = adopterService.createAdopter(newAdopter);

        assertNotNull(result);
        AdopterEntity stored = entityManager.find(AdopterEntity.class, result.getId());
        assertEquals(newAdopter.getEmail(), stored.getEmail());
    }

    @Test
    void testCreateAdopterWithoutPhone() {
        AdopterEntity newAdopter = factory.manufacturePojo(AdopterEntity.class);
        newAdopter.setEmail("invalidadopter@mail.com");
        newAdopter.setName("Invalid Adopter");
        newAdopter.setPassword("Secure123");
        newAdopter.setPhone("");

        assertThrows(IllegalOperationException.class, () -> adopterService.createAdopter(newAdopter));
    }

    @Test
    void testSearchAdopter() throws Exception {
        AdopterEntity found = adopterService.searchAdopter(adopterList.get(0).getId());
        assertNotNull(found);
        assertEquals(adopterList.get(0).getId(), found.getId());
    }

    @Test
    void testSearchInvalidAdopter() {
        assertThrows(EntityNotFoundException.class, () -> adopterService.searchAdopter(0L));
    }

    @Test
    void testSearchAdopters() {
        List<AdopterEntity> adopters = adopterService.searchAdopters();
        assertEquals(adopterList.size(), adopters.size());
    }

    @Test
    void testUpdateAdopter() throws Exception {
        AdopterEntity newData = factory.manufacturePojo(AdopterEntity.class);
        newData.setName("Updated Adopter");
        newData.setEmail("updatedadopter@mail.com");
        newData.setPassword("Updated123");
        newData.setPhone("3110000000");

        AdopterEntity updated = adopterService.updateAdopter(adopterList.get(0).getId(), newData);

        assertEquals("Updated Adopter", updated.getName());
        assertEquals("updatedadopter@mail.com", updated.getEmail());
    }

    @Test
    void testDeleteAdopterWithAssociations() {
        AdopterEntity adopter = adopterList.get(0);
        NotificationEntity notification = factory.manufacturePojo(NotificationEntity.class);
        notification.setContent("Notification");
        notification.setUser(adopter);
        entityManager.persist(notification);

        assertThrows(IllegalOperationException.class, () -> adopterService.deleteAdopter(adopter.getId()));
    }

    @Test
    void testDeleteAdopter() throws Exception {
        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        adopter.setEmail("deleteadopter@mail.com");
        adopter.setName("Delete Adopter");
        adopter.setPassword("Secure123");
        adopter.setPhone("3220000000");
        entityManager.persist(adopter);

        adopterService.deleteAdopter(adopter.getId());
        AdopterEntity deleted = entityManager.find(AdopterEntity.class, adopter.getId());
        assertTrue(deleted == null);
    }
}
