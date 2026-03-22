package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.entities.UserEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.NotificationRepository;
import co.edu.udistrital.mdp.pets.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {
    private static final String NOT_FOUND_MSG = NOT_FOUND_MSG;

    private static final String NULL_NOTIF_ID = NULL_NOTIF_ID;

    private static final String NOTIF_NOT_FOUND = NOT_FOUND_MSG;
    private static final String NOTIF_ID_NULL = NULL_NOTIF_ID;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public NotificationEntity createNotification(NotificationEntity notificationEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación de la notificación");
        validateNotificationData(notificationEntity);
        Long safeUserId = Objects.requireNonNull(notificationEntity.getUser().getId(), "userId must not be null");
        UserEntity user = userRepository.findById(safeUserId)
                .orElseThrow(() -> new EntityNotFoundException("The user with the given id was not found"));
        notificationEntity.setUser(user);
        if (notificationEntity.getCreatedAt() == null) {
            notificationEntity.setCreatedAt(LocalDateTime.now());
        }
        if (notificationEntity.getIsRead() == null) {
            notificationEntity.setIsRead(Boolean.FALSE);
        }
        log.info("Termina proceso de creación de la notificación");
        return notificationRepository.save(notificationEntity);
    }

    @Transactional
    public NotificationEntity searchNotification(Long notificationId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la notificación con id = {}", notificationId);
        Long safeNotificationId = Objects.requireNonNull(notificationId, NOTIF_ID_NULL);
        Optional<NotificationEntity> notificationEntity = notificationRepository.findById(safeNotificationId);
        if (notificationEntity.isEmpty()) {
            throw new EntityNotFoundException(NOTIF_NOT_FOUND);
        }
        log.info("Termina proceso de consultar la notificación con id = {}", notificationId);
        return notificationEntity.get();
    }

    @Transactional
    public List<NotificationEntity> searchNotifications() {
        log.info("Inicia proceso de consultar todas las notificaciones");
        return notificationRepository.findAll();
    }

    @Transactional
    public NotificationEntity updateNotification(Long notificationId, NotificationEntity notificationEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la notificación con id = {}", notificationId);
        Long safeNotificationId = Objects.requireNonNull(notificationId, NOTIF_ID_NULL);
        Optional<NotificationEntity> persistedNotification = notificationRepository.findById(safeNotificationId);
        if (persistedNotification.isEmpty()) {
            throw new EntityNotFoundException(NOTIF_NOT_FOUND);
        }
        if (notificationEntity == null || notificationEntity.getContent() == null || notificationEntity.getContent().isBlank()) {
            throw new IllegalOperationException("Notification content is not valid");
        }
        NotificationEntity storedNotification = persistedNotification.get();
        storedNotification.setTitle(notificationEntity.getTitle());
        storedNotification.setContent(notificationEntity.getContent());
        storedNotification.setIsRead(notificationEntity.getIsRead() != null ? notificationEntity.getIsRead() : storedNotification.getIsRead());
        log.info("Termina proceso de actualizar la notificación con id = {}", notificationId);
        return notificationRepository.save(storedNotification);
    }

    @Transactional
    public void deleteNotification(Long notificationId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar la notificación con id = {}", notificationId);
        Long safeNotificationId = Objects.requireNonNull(notificationId, NOTIF_ID_NULL);
        Optional<NotificationEntity> notificationEntity = notificationRepository.findById(safeNotificationId);
        if (notificationEntity.isEmpty()) {
            throw new EntityNotFoundException(NOTIF_NOT_FOUND);
        }
        if (Boolean.FALSE.equals(notificationEntity.get().getIsRead())) {
            throw new IllegalOperationException("Unread notifications cannot be deleted");
        }
        log.info("Termina proceso de borrar la notificación con id = {}", notificationId);
        notificationRepository.deleteById(safeNotificationId);
    }

    private void validateNotificationData(NotificationEntity notificationEntity) throws IllegalOperationException {
        if (notificationEntity == null) {
            throw new IllegalOperationException("Notification is not valid");
        }
        if (notificationEntity.getContent() == null || notificationEntity.getContent().isBlank()) {
            throw new IllegalOperationException("Notification content is not valid");
        }
        if (notificationEntity.getUser() == null || notificationEntity.getUser().getId() == null) {
            throw new IllegalOperationException("Notification user is not valid");
        }
    }
}

