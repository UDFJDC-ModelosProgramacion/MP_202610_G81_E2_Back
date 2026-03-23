package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import co.edu.udistrital.mdp.pets.entities.UserEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MessageRepository;
import co.edu.udistrital.mdp.pets.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService {
    private static final String NULL_MSG_ID = "messageId must not be null";

    private static final String MSG_ID_NULL = NULL_MSG_ID;

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public MessageEntity createMessage(MessageEntity messageEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de creación del mensaje");
        validateMessageData(messageEntity);
        UserEntity sender = validateAndGetUser(messageEntity.getSender());
        UserEntity receiver = validateAndGetUser(messageEntity.getReceiver());
        messageEntity.setSender(sender);
        messageEntity.setReceiver(receiver);
        if (messageEntity.getSentAt() == null) {
            messageEntity.setSentAt(LocalDateTime.now());
        }
        if (messageEntity.getIsRead() == null) {
            messageEntity.setIsRead(Boolean.FALSE);
        }
        messageEntity.setActive(Boolean.TRUE);
        log.info("Termina proceso de creación del mensaje");
        return messageRepository.save(messageEntity);
    }

    @Transactional
    public MessageEntity searchMessage(Long messageId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar el mensaje con id = {}", messageId);
        Long safeMessageId = Objects.requireNonNull(messageId, MSG_ID_NULL);
        Optional<MessageEntity> messageEntity = messageRepository.findById(safeMessageId);
        if (messageEntity.isEmpty()) {
            throw new EntityNotFoundException("The message with the given id was not found");
        }
        log.info("Termina proceso de consultar el mensaje con id = {}", messageId);
        return messageEntity.get();
    }

    @Transactional
    public List<MessageEntity> searchMessages() {
        log.info("Inicia proceso de consultar todos los mensajes");
        return messageRepository.findByActiveTrue();
    }

    @Transactional
    public MessageEntity updateMessage(Long messageId, MessageEntity messageEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar el mensaje con id = {}", messageId);
        Long safeMessageId = Objects.requireNonNull(messageId, MSG_ID_NULL);
        Optional<MessageEntity> persistedMessage = messageRepository.findById(safeMessageId);
        if (persistedMessage.isEmpty() || !Boolean.TRUE.equals(persistedMessage.get().getActive())) {
            throw new EntityNotFoundException("The message with the given id was not found or is deleted");
        }
        MessageEntity storedMessage = Objects.requireNonNull(persistedMessage.get(), "Stored message cannot be null");
        if (messageEntity != null && messageEntity.getIsRead() != null) {
            storedMessage.setIsRead(messageEntity.getIsRead());
        }
        log.info("Termina proceso de actualizar el mensaje con id = {}", messageId);
        return messageRepository.save(storedMessage);
    }

    @Transactional
    public void deleteMessage(Long messageId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar el mensaje con id = {}", messageId);
        Long safeMessageId = Objects.requireNonNull(messageId, MSG_ID_NULL);
        Optional<MessageEntity> messageEntity = messageRepository.findById(safeMessageId);
        if (messageEntity.isEmpty() || !Boolean.TRUE.equals(messageEntity.get().getActive())) {
            throw new EntityNotFoundException("The message with the given id was not found");
        }
        if (Boolean.TRUE.equals(messageEntity.get().getIsRead())) {
            throw new IllegalOperationException("Read messages cannot be deleted");
        }
        MessageEntity storedMessage = Objects.requireNonNull(messageEntity.get(), "Stored message cannot be null");
        storedMessage.setActive(Boolean.FALSE);
        messageRepository.save(storedMessage);
        log.info("Termina proceso de borrar el mensaje con id = {}", messageId);
    }

    private void validateMessageData(MessageEntity messageEntity) throws IllegalOperationException {
        if (messageEntity == null) {
            throw new IllegalOperationException("Message is not valid");
        }
        if (messageEntity.getContent() == null || messageEntity.getContent().isBlank()) {
            throw new IllegalOperationException("Message content is not valid");
        }
        if (messageEntity.getSender() == null || messageEntity.getSender().getId() == null) {
            throw new IllegalOperationException("Message sender is not valid");
        }
        if (messageEntity.getReceiver() == null || messageEntity.getReceiver().getId() == null) {
            throw new IllegalOperationException("Message receiver is not valid");
        }
        if (messageEntity.getSender().getId().equals(messageEntity.getReceiver().getId())) {
            throw new IllegalOperationException("A user cannot send a message to themselves");
        }
    }

    private UserEntity validateAndGetUser(UserEntity userEntity) throws EntityNotFoundException {
        Long safeUserId = Objects.requireNonNull(userEntity.getId(), "userId must not be null");
        return userRepository.findById(safeUserId)
                .orElseThrow(() -> new EntityNotFoundException("The user with the given id was not found"));
    }
}
