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
@Import(NotificationService.class)
class NotificationServiceTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<AdopterEntity> userList = new ArrayList<>();
    private final List<NotificationEntity> notificationList = new ArrayList<>();

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
            AdopterEntity userEntity = factory.manufacturePojo(AdopterEntity.class);
            userEntity.setEmail("notificationuser" + i + "@mail.com");
            userEntity.setName("Notification User " + i);
            userEntity.setPassword("Password123");
            userEntity.setPhone("30000000" + i);
            entityManager.persist(userEntity);
            userList.add(userEntity);
        }

        for (int i = 0; i < 3; i++) {
            NotificationEntity notificationEntity = factory.manufacturePojo(NotificationEntity.class);
            notificationEntity.setTitle("Title " + i);
            notificationEntity.setContent("Notification " + i);
            notificationEntity.setUser(userList.get(0));
            notificationEntity.setRead(Boolean.TRUE);
            entityManager.persist(notificationEntity);
            notificationList.add(notificationEntity);
        }
    }

    @Test
    void testCreateNotification() throws Exception {
        NotificationEntity newNotification = factory.manufacturePojo(NotificationEntity.class);
        newNotification.setTitle("New notification");
        newNotification.setContent("New content");
        newNotification.setUser(userList.get(0));
        newNotification.setRead(Boolean.FALSE);

        NotificationEntity result = notificationService.createNotification(newNotification);

        assertNotNull(result);
        NotificationEntity stored = entityManager.find(NotificationEntity.class, result.getId());
        assertEquals("New content", stored.getContent());
    }

    @Test
    void testCreateNotificationWithoutContent() {
        NotificationEntity newNotification = factory.manufacturePojo(NotificationEntity.class);
        newNotification.setTitle("Invalid");
        newNotification.setContent("");
        newNotification.setUser(userList.get(0));

        assertThrows(IllegalOperationException.class, () -> notificationService.createNotification(newNotification));
    }

    @Test
    void testSearchNotification() throws Exception {
        NotificationEntity found = notificationService.searchNotification(notificationList.get(0).getId());
        assertEquals(notificationList.get(0).getId(), found.getId());
    }

    @Test
    void testSearchInvalidNotification() {
        assertThrows(EntityNotFoundException.class, () -> notificationService.searchNotification(0L));
    }

    @Test
    void testSearchNotifications() {
        List<NotificationEntity> notifications = notificationService.searchNotifications();
        assertEquals(notificationList.size(), notifications.size());
    }

    @Test
    void testUpdateNotification() throws Exception {
        NotificationEntity newData = factory.manufacturePojo(NotificationEntity.class);
        newData.setTitle("Updated title");
        newData.setContent("Updated notification");
        newData.setRead(Boolean.TRUE);

        NotificationEntity updated = notificationService.updateNotification(notificationList.get(0).getId(), newData);

        assertEquals("Updated notification", updated.getContent());
        assertTrue(updated.getRead());
    }

    @Test
    void testDeleteUnreadNotification() {
        NotificationEntity unreadNotification = factory.manufacturePojo(NotificationEntity.class);
        unreadNotification.setTitle("Unread");
        unreadNotification.setContent("Unread notification");
        unreadNotification.setUser(userList.get(0));
        unreadNotification.setRead(Boolean.FALSE);
        entityManager.persist(unreadNotification);

        assertThrows(IllegalOperationException.class, () -> notificationService.deleteNotification(unreadNotification.getId()));
    }

    @Test
    void testDeleteNotification() throws Exception {
        NotificationEntity deletable = factory.manufacturePojo(NotificationEntity.class);
        deletable.setTitle("Delete");
        deletable.setContent("Delete notification");
        deletable.setUser(userList.get(0));
        deletable.setRead(Boolean.TRUE);
        entityManager.persist(deletable);

        notificationService.deleteNotification(deletable.getId());
        NotificationEntity deleted = entityManager.find(NotificationEntity.class, deletable.getId());
        assertTrue(deleted == null);
    }
}
