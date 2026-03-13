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

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.entities.UserEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import org.springframework.transaction.annotation.Transactional;
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
            userEntity.setPassword("Password123");
            userEntity.setPhoneNumber("30000000" + i);
            entityManager.persist(userEntity);
            userList.add(userEntity);
        }

        for (int i = 0; i < 3; i++) {
            NotificationEntity notificationEntity = factory.manufacturePojo(NotificationEntity.class);
            notificationEntity.setTitle("Title " + i);
            notificationEntity.setContent("Notification " + i);
            notificationEntity.setUser(userList.get(0));
            notificationEntity.setIsRead(Boolean.TRUE);
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
        newNotification.setIsRead(Boolean.FALSE);

        NotificationEntity result = notificationService.createNotification(newNotification);

        assertNotNull(result);
        NotificationEntity stored = entityManager.find(NotificationEntity.class, result.getId());
        assertEquals(newNotification.getTitle(), stored.getTitle());
        assertEquals(newNotification.getContent(), stored.getContent());
        assertEquals(newNotification.getIsRead(), stored.getIsRead());
        assertEquals(userList.get(0).getId(), stored.getUser().getId());
    }

    @Test
    void testCreateNotificationNull() {
        assertThrows(IllegalOperationException.class, () -> notificationService.createNotification(null));
    }

    @Test
    void testCreateNotificationWithNullUser() {
        assertThrows(IllegalOperationException.class, () -> {
            NotificationEntity newNotification = factory.manufacturePojo(NotificationEntity.class);
            newNotification.setTitle("No user");
            newNotification.setContent("Content");
            newNotification.setUser(null);
            notificationService.createNotification(newNotification);
        });
    }

    @Test
    void testCreateNotificationWithInvalidUser() {
        assertThrows(EntityNotFoundException.class, () -> {
            NotificationEntity newNotification = factory.manufacturePojo(NotificationEntity.class);
            newNotification.setTitle("Invalid user");
            newNotification.setContent("Content");
            UserEntity invalidUser = new AdopterEntity();
            invalidUser.setId(0L);
            newNotification.setUser(invalidUser);
            notificationService.createNotification(newNotification);
        });
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
        NotificationEntity entity = notificationList.get(0);
        NotificationEntity resultEntity = notificationService.searchNotification(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getTitle(), resultEntity.getTitle());
        assertEquals(entity.getContent(), resultEntity.getContent());
        assertEquals(entity.getIsRead(), resultEntity.getIsRead());
    }

    @Test
    void testSearchInvalidNotification() {
        assertThrows(EntityNotFoundException.class, () -> notificationService.searchNotification(0L));
    }

    @Test
    void testSearchNotifications() {
        List<NotificationEntity> notifications = notificationService.searchNotifications();
        assertEquals(notificationList.size(), notifications.size());
        for (NotificationEntity entity : notifications) {
            boolean found = false;
            for (NotificationEntity storedEntity : notificationList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testUpdateNotification() throws Exception {
        NotificationEntity newData = factory.manufacturePojo(NotificationEntity.class);
        newData.setTitle("Updated title");
        newData.setContent("Updated notification");
        newData.setIsRead(Boolean.TRUE);

        notificationService.updateNotification(notificationList.get(0).getId(), newData);

        NotificationEntity stored = entityManager.find(NotificationEntity.class, notificationList.get(0).getId());
        assertEquals("Updated title", stored.getTitle());
        assertEquals("Updated notification", stored.getContent());
        assertEquals(Boolean.TRUE, stored.getIsRead());
    }

    @Test
    void testUpdateNotificationNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            NotificationEntity newData = factory.manufacturePojo(NotificationEntity.class);
            newData.setContent("Updated notification");
            notificationService.updateNotification(0L, newData);
        });
    }

    @Test
    void testDeleteUnreadNotification() {
        NotificationEntity unreadNotification = factory.manufacturePojo(NotificationEntity.class);
        unreadNotification.setTitle("Unread");
        unreadNotification.setContent("Unread notification");
        unreadNotification.setUser(userList.get(0));
        unreadNotification.setIsRead(Boolean.FALSE);
        entityManager.persist(unreadNotification);

        assertThrows(IllegalOperationException.class, () -> notificationService.deleteNotification(unreadNotification.getId()));
    }

    @Test
    void testDeleteNotification() throws Exception {
        NotificationEntity deletable = factory.manufacturePojo(NotificationEntity.class);
        deletable.setTitle("Delete");
        deletable.setContent("Delete notification");
        deletable.setUser(userList.get(0));
        deletable.setIsRead(Boolean.TRUE);
        entityManager.persist(deletable);

        notificationService.deleteNotification(deletable.getId());
        NotificationEntity deleted = entityManager.find(NotificationEntity.class, deletable.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteInvalidNotification() {
        assertThrows(EntityNotFoundException.class, () -> notificationService.deleteNotification(0L));
    }
}
