package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.entities.UserEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.NotificationRepository;
import co.edu.udistrital.mdp.pets.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public NotificationEntity createNotification(NotificationEntity notificationEntity)
            throws EntityNotFoundException, IllegalOperationException {
        validateNotificationData(notificationEntity);
        UserEntity user = userRepository.findById(notificationEntity.getUser().getId())
                .orElseThrow(() -> new EntityNotFoundException("The user with the given id was not found"));
        notificationEntity.setUser(user);
        if (notificationEntity.getCreatedAt() == null) {
            notificationEntity.setCreatedAt(LocalDateTime.now());
        }
        if (notificationEntity.getRead() == null) {
            notificationEntity.setRead(Boolean.FALSE);
        }
        return notificationRepository.save(notificationEntity);
    }

    @Transactional
    public NotificationEntity searchNotification(Long notificationId) throws EntityNotFoundException {
        Optional<NotificationEntity> notificationEntity = notificationRepository.findById(notificationId);
        if (notificationEntity.isEmpty()) {
            throw new EntityNotFoundException("The notification with the given id was not found");
        }
        return notificationEntity.get();
    }

    @Transactional
    public List<NotificationEntity> searchNotifications() {
        return notificationRepository.findAll();
    }

    @Transactional
    public NotificationEntity updateNotification(Long notificationId, NotificationEntity notificationEntity)
            throws EntityNotFoundException, IllegalOperationException {
        Optional<NotificationEntity> persistedNotification = notificationRepository.findById(notificationId);
        if (persistedNotification.isEmpty()) {
            throw new EntityNotFoundException("The notification with the given id was not found");
        }
        if (notificationEntity == null || notificationEntity.getContent() == null || notificationEntity.getContent().isBlank()) {
            throw new IllegalOperationException("Notification content is not valid");
        }
        NotificationEntity storedNotification = persistedNotification.get();
        storedNotification.setTitle(notificationEntity.getTitle());
        storedNotification.setContent(notificationEntity.getContent());
        storedNotification.setRead(notificationEntity.getRead() != null ? notificationEntity.getRead() : storedNotification.getRead());
        return notificationRepository.save(storedNotification);
    }

    @Transactional
    public void deleteNotification(Long notificationId) throws EntityNotFoundException, IllegalOperationException {
        Optional<NotificationEntity> notificationEntity = notificationRepository.findById(notificationId);
        if (notificationEntity.isEmpty()) {
            throw new EntityNotFoundException("The notification with the given id was not found");
        }
        if (Boolean.FALSE.equals(notificationEntity.get().getRead())) {
            throw new IllegalOperationException("Unread notifications cannot be deleted");
        }
        notificationRepository.deleteById(notificationId);
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

