package co.edu.udistrital.mdp.pets.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
class NotificationEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private NotificationEntity notificationEntity;

    @BeforeEach
    void setUp() {
        notificationEntity = factory.manufacturePojo(NotificationEntity.class);
    }

    @Test
    void testCreateNotification() {
        NotificationEntity saved = entityManager.persistAndFlush(notificationEntity);
        NotificationEntity found = entityManager.find(NotificationEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(notificationEntity.getTitle(), found.getTitle());
        assertEquals(notificationEntity.getContent(), found.getContent());
        assertEquals(notificationEntity.getCreatedAt(), found.getCreatedAt());
        assertEquals(notificationEntity.getIsRead(), found.getIsRead());
    }

    @Test
    void testNotificationWithUser() {
        AdopterEntity user = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persistAndFlush(user);

        notificationEntity.setUser(user);
        NotificationEntity saved = entityManager.persistAndFlush(notificationEntity);

        NotificationEntity found = entityManager.find(NotificationEntity.class, saved.getId());
        assertNotNull(found);
        assertNotNull(found.getUser());
        assertEquals(user.getId(), found.getUser().getId());
    }
}
