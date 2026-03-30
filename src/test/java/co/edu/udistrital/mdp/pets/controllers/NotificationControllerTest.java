package co.edu.udistrital.mdp.pets.controllers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import co.edu.udistrital.mdp.pets.dto.NotificationDTO;
import co.edu.udistrital.mdp.pets.dto.NotificationDetailDTO;
import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.NotificationService;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@ExtendWith(MockitoExtension.class)
class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    private PodamFactory factory = new PodamFactoryImpl();
    private NotificationEntity notificationEntity;
    private NotificationDTO notificationDTO;

    @BeforeEach
    void setUp() {
        notificationEntity = factory.manufacturePojo(NotificationEntity.class);
        notificationEntity.setUser(null);
        notificationDTO = new NotificationDTO(notificationEntity);
    }

    @Test
    void testFindAll() {
        List<NotificationEntity> notifications = new ArrayList<>();
        notifications.add(notificationEntity);

        when(notificationService.searchNotifications()).thenReturn(notifications);

        List<NotificationDTO> result = notificationController.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(notificationEntity.getId(), result.get(0).getId());
    }

    @Test
    void testFindOne() throws EntityNotFoundException {
        when(notificationService.searchNotification(notificationEntity.getId())).thenReturn(notificationEntity);

        NotificationDetailDTO result = notificationController.findOne(notificationEntity.getId());

        assertNotNull(result);
        assertEquals(notificationEntity.getId(), result.getId());
    }

    @Test
    void testCreate() throws EntityNotFoundException, IllegalOperationException {
        when(notificationService.createNotification(any(NotificationEntity.class))).thenReturn(notificationEntity);

        NotificationDetailDTO result = notificationController.create(notificationDTO);

        assertNotNull(result);
        assertEquals(notificationDTO.getId(), result.getId());
    }

    @Test
    void testUpdate() throws EntityNotFoundException, IllegalOperationException {
        when(notificationService.updateNotification(eq(notificationEntity.getId()), any(NotificationEntity.class))).thenReturn(notificationEntity);

        NotificationDetailDTO result = notificationController.update(notificationEntity.getId(), notificationDTO);

        assertNotNull(result);
        assertEquals(notificationDTO.getId(), result.getId());
    }

    @Test
    void testDelete() throws EntityNotFoundException, IllegalOperationException {
        doNothing().when(notificationService).deleteNotification(notificationEntity.getId());

        notificationController.delete(notificationEntity.getId());

        verify(notificationService).deleteNotification(notificationEntity.getId());
    }
}