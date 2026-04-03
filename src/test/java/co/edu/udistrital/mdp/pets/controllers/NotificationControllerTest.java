package co.edu.udistrital.mdp.pets.controllers;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.dto.AdopterDTO;
import co.edu.udistrital.mdp.pets.dto.NotificationDTO;
import co.edu.udistrital.mdp.pets.dto.NotificationDetailDTO;
import co.edu.udistrital.mdp.pets.dto.UserDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

@SpringBootTest
@Transactional
class NotificationControllerTest {

    @Autowired
    private NotificationController notificationController;

    @Autowired
    private AdopterController adopterController;

    private NotificationDTO notificationDTO;
    private Long userId;

    @SuppressWarnings("java:S1144")
    void initializeTestData() throws IllegalOperationException {
        AdopterDTO user = new AdopterDTO();
        user.setName("Notification User");
        user.setEmail("notification@test.com");
        user.setPassword("password123");
        user.setAddress("123 Main St");
        user.setCity("Bogota");
        userId = adopterController.create(user).getId();

        notificationDTO = new NotificationDTO();
        notificationDTO.setTitle("Test Title");
        notificationDTO.setContent("Test Content");
        notificationDTO.setIsRead(false);
        notificationDTO.setCreatedAt(LocalDateTime.now());

        UserDTO userDTO = new UserDTO();
        userDTO.setId(userId);
        notificationDTO.setUser(userDTO);
    }

    @Test
    void testCreateNotification() throws IllegalOperationException, EntityNotFoundException {
        initializeTestData();
        NotificationDetailDTO createdNotification = notificationController.create(notificationDTO);

        assertNotNull(createdNotification);
        assertNotNull(createdNotification.getId());
        assertEquals(notificationDTO.getTitle(), createdNotification.getTitle());
    }

    @Test
    void testFindAll() throws IllegalOperationException, EntityNotFoundException {
        initializeTestData();
        notificationController.create(notificationDTO);

        List<NotificationDTO> notifications = notificationController.findAll(null);
        assertNotNull(notifications);
        assertFalse(notifications.isEmpty());
    }

    @Test
    void testFindOne() throws IllegalOperationException, EntityNotFoundException {
        initializeTestData();
        NotificationDetailDTO createdNotification = notificationController.create(notificationDTO);

        NotificationDetailDTO foundNotification = notificationController.findOne(createdNotification.getId());
        assertNotNull(foundNotification);
        assertEquals(createdNotification.getId(), foundNotification.getId());
        assertEquals("Test Title", foundNotification.getTitle());
    }

    @Test
    void testUpdate() throws IllegalOperationException, EntityNotFoundException {
        initializeTestData();
        NotificationDetailDTO createdNotification = notificationController.create(notificationDTO);

        notificationDTO.setIsRead(true);
        NotificationDetailDTO updatedNotification = notificationController.update(createdNotification.getId(), notificationDTO);

        assertNotNull(updatedNotification);
        assertTrue(updatedNotification.getIsRead());
    }

    @Test
    void testDelete() throws IllegalOperationException, EntityNotFoundException {
        initializeTestData();
        NotificationDetailDTO createdNotification = notificationController.create(notificationDTO);

        notificationController.markAsReadWithPatch(createdNotification.getId());

        notificationController.delete(createdNotification.getId());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            notificationController.findOne(createdNotification.getId());
        });
        assertNotNull(exception);
    }
}
