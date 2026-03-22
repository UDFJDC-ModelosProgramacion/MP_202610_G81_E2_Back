package co.edu.udistrital.mdp.pets.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
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
    @SuppressWarnings("unused")
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
            adopterEntity.setName("Adopter " + i);
            adopterEntity.setEmail("adopter" + i + "@mail.com");
            adopterEntity.setPassword("Password123");
            adopterEntity.setPhoneNumber("30000000" + i);
            adopterEntity.setAddress("Calle " + i);
            adopterEntity.setCity("City " + i);
            adopterEntity.setStatus("Active");
            entityManager.persist(adopterEntity);
            adopterList.add(adopterEntity);
        }
    }

    @Test
    void testCreateAdopter() throws IllegalOperationException {
        AdopterEntity newAdopter = factory.manufacturePojo(AdopterEntity.class);
        newAdopter.setEmail("newadopter@mail.com");
        newAdopter.setPassword("Secure123");
        newAdopter.setPhoneNumber("3001111111");

        AdopterEntity result = adopterService.createAdopter(newAdopter);

        assertNotNull(result);
        AdopterEntity stored = entityManager.find(AdopterEntity.class, result.getId());
        assertEquals(newAdopter.getId(), stored.getId());
        assertEquals(newAdopter.getEmail(), stored.getEmail());
        assertEquals(newAdopter.getPassword(), stored.getPassword());
        assertEquals(newAdopter.getPhoneNumber(), stored.getPhoneNumber());
        assertEquals(newAdopter.getAddress(), stored.getAddress());
        assertEquals(newAdopter.getCity(), stored.getCity());
        assertEquals(newAdopter.getHousingType(), stored.getHousingType());
    }

    @Test
    void testCreateAdopterNull() {
        assertNotNull(assertThrows(IllegalOperationException.class, () -> adopterService.createAdopter(null)));
    }

    @Test
    void testCreateAdopterWithInvalidEmail() {
        assertNotNull(assertThrows(IllegalOperationException.class, () -> {
            AdopterEntity newAdopter = factory.manufacturePojo(AdopterEntity.class);
            newAdopter.setEmail("");
            newAdopter.setPassword("Secure123");
            newAdopter.setPhoneNumber("3001111111");
            adopterService.createAdopter(newAdopter);
        }));
    }

    @Test
    void testCreateAdopterWithInvalidPassword() {
        assertNotNull(assertThrows(IllegalOperationException.class, () -> {
            AdopterEntity newAdopter = factory.manufacturePojo(AdopterEntity.class);
            newAdopter.setEmail("valid@mail.com");
            newAdopter.setPassword("");
            newAdopter.setPhoneNumber("3001111111");
            adopterService.createAdopter(newAdopter);
        }));
    }

    @Test
    void testCreateAdopterWithoutPhone() {
        AdopterEntity newAdopter = factory.manufacturePojo(AdopterEntity.class);
        newAdopter.setEmail("invalidadopter@mail.com");
        newAdopter.setPassword("Secure123");
        newAdopter.setPhoneNumber("");

        assertNotNull(assertThrows(IllegalOperationException.class, () -> adopterService.createAdopter(newAdopter)));
    }

    @Test
    void testCreateAdopterDuplicatedEmail() {
        AdopterEntity newAdopter = factory.manufacturePojo(AdopterEntity.class);
        newAdopter.setEmail(adopterList.get(0).getEmail());
        assertNotNull(assertThrows(IllegalOperationException.class, () -> adopterService.createAdopter(newAdopter)));
    }

    @Test
    void testCreateAdopterWithoutNameAddressCity() {
        assertNotNull(assertThrows(IllegalOperationException.class, () -> {
            AdopterEntity newAdopter = factory.manufacturePojo(AdopterEntity.class);
            newAdopter.setName("");
            adopterService.createAdopter(newAdopter);
        }));
        assertNotNull(assertThrows(IllegalOperationException.class, () -> {
            AdopterEntity newAdopter = factory.manufacturePojo(AdopterEntity.class);
            newAdopter.setAddress("");
            adopterService.createAdopter(newAdopter);
        }));
        assertNotNull(assertThrows(IllegalOperationException.class, () -> {
            AdopterEntity newAdopter = factory.manufacturePojo(AdopterEntity.class);
            newAdopter.setCity("");
            adopterService.createAdopter(newAdopter);
        }));
    }

    @Test
    void testSearchAdopter() throws EntityNotFoundException {
        AdopterEntity entity = adopterList.get(0);
        AdopterEntity resultEntity = adopterService.searchAdopter(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getEmail(), resultEntity.getEmail());
        assertEquals(entity.getPassword(), resultEntity.getPassword());
        assertEquals(entity.getPhoneNumber(), resultEntity.getPhoneNumber());
        assertEquals(entity.getAddress(), resultEntity.getAddress());
        assertEquals(entity.getCity(), resultEntity.getCity());
        assertEquals(entity.getHousingType(), resultEntity.getHousingType());
    }

    @Test
    void testSearchInvalidAdopter() {
        assertNotNull(assertThrows(EntityNotFoundException.class, () -> adopterService.searchAdopter(0L)));
    }

    @Test
    void testSearchAdopters() {
        List<AdopterEntity> adopters = adopterService.searchAdopters();
        assertEquals(adopterList.size(), adopters.size());
        for (AdopterEntity entity : adopters) {
            boolean found = false;
            for (AdopterEntity storedEntity : adopterList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testSearchAdoptersWithFilters() {
        List<AdopterEntity> resultsName = adopterService.searchAdopters(adopterList.get(0).getName(), null, null);
        assertTrue(resultsName.size() > 0);

        List<AdopterEntity> resultsEmail = adopterService.searchAdopters(null, adopterList.get(0).getEmail(), null);
        assertTrue(resultsEmail.size() > 0);

        List<AdopterEntity> resultsStatus = adopterService.searchAdopters(null, null, "Active");
        assertTrue(resultsStatus.size() > 0);
    }

    @Test
    void testUpdateAdopter() throws EntityNotFoundException, IllegalOperationException {
        AdopterEntity pojoEntity = factory.manufacturePojo(AdopterEntity.class);
        pojoEntity.setEmail("updatedadopter@mail.com");
        pojoEntity.setPassword("Updated123");
        pojoEntity.setPhoneNumber("3110000000");

        adopterService.updateAdopter(adopterList.get(0).getId(), pojoEntity);

        AdopterEntity stored = entityManager.find(AdopterEntity.class, adopterList.get(0).getId());
        assertEquals(pojoEntity.getEmail(), stored.getEmail());
        assertEquals(pojoEntity.getPassword(), stored.getPassword());
        assertEquals(pojoEntity.getPhoneNumber(), stored.getPhoneNumber());
        assertEquals(pojoEntity.getAddress(), stored.getAddress());
        assertEquals(pojoEntity.getCity(), stored.getCity());
        assertEquals(pojoEntity.getHousingType(), stored.getHousingType());
    }

    @Test
    void testUpdateAdopterNotFound() {
        assertNotNull(assertThrows(EntityNotFoundException.class, () -> {
            AdopterEntity pojoEntity = factory.manufacturePojo(AdopterEntity.class);
            pojoEntity.setEmail("updated@mail.com");
            pojoEntity.setPassword("Updated123");
            pojoEntity.setPhoneNumber("3110000000");
            adopterService.updateAdopter(0L, pojoEntity);
        }));
    }

    @Test
    void testUpdateAdopterDuplicatedEmail() {
        AdopterEntity pojoEntity = factory.manufacturePojo(AdopterEntity.class);
        pojoEntity.setEmail(adopterList.get(1).getEmail());
        pojoEntity.setPassword("Updated123");
        pojoEntity.setPhoneNumber("3110000000");
        assertNotNull(assertThrows(IllegalOperationException.class, () -> {
            adopterService.updateAdopter(adopterList.get(0).getId(), pojoEntity);
        }));
    }

    @Test
    void testDeleteAdopterWithAssociations() {
        AdopterEntity adopter = adopterList.get(0);
        co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity request = factory.manufacturePojo(co.edu.udistrital.mdp.pets.entities.AdoptionRequestEntity.class);
        request.setAdopter(adopter);
        entityManager.persist(request);
        adopter.getAdoptionRequests().add(request);

        assertNotNull(assertThrows(IllegalOperationException.class, () -> adopterService.deleteAdopter(adopter.getId())));
    }

    @Test
    void testDeleteAdopter() throws EntityNotFoundException, IllegalOperationException {
        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        adopter.setEmail("deleteadopter@mail.com");
        adopter.setPassword("Secure123");
        adopter.setPhoneNumber("3220000000");
        adopter.setSentMessages(new ArrayList<>());
        adopter.setReceivedMessages(new ArrayList<>());
        adopter.setNotifications(new ArrayList<>());
        entityManager.persist(adopter);

        adopterService.deleteAdopter(adopter.getId());
        AdopterEntity deleted = entityManager.find(AdopterEntity.class, adopter.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteInvalidAdopter() {
        assertNotNull(assertThrows(EntityNotFoundException.class, () -> adopterService.deleteAdopter(0L)));
    }
}
