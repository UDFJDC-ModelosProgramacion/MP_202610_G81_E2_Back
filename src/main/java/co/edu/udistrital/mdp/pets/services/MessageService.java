package co.edu.udistrital.mdp.pets.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import co.edu.udistrital.mdp.pets.entities.UserEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.MessageRepository;
import co.edu.udistrital.mdp.pets.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MessageService {

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
        return messageRepository.save(messageEntity);
    }

    @Transactional
    public MessageEntity searchMessage(Long messageId) throws EntityNotFoundException {
        Optional<MessageEntity> messageEntity = messageRepository.findById(messageId);
        if (messageEntity.isEmpty()) {
            throw new EntityNotFoundException("The message with the given id was not found");
        }
        return messageEntity.get();
    }

    @Transactional
    public List<MessageEntity> searchMessages() {
        return messageRepository.findAll();
    }

    @Transactional
    public MessageEntity updateMessage(Long messageId, MessageEntity messageEntity)
            throws EntityNotFoundException, IllegalOperationException {
        Optional<MessageEntity> persistedMessage = messageRepository.findById(messageId);
        if (persistedMessage.isEmpty()) {
            throw new EntityNotFoundException("The message with the given id was not found");
        }
        if (messageEntity == null || messageEntity.getContent() == null || messageEntity.getContent().isBlank()) {
            throw new IllegalOperationException("Message content is not valid");
        }
        MessageEntity storedMessage = persistedMessage.get();
        storedMessage.setContent(messageEntity.getContent());
        storedMessage.setIsRead(messageEntity.getIsRead() != null ? messageEntity.getIsRead() : storedMessage.getIsRead());
        return messageRepository.save(storedMessage);
    }

    @Transactional
    public void deleteMessage(Long messageId) throws EntityNotFoundException, IllegalOperationException {
        Optional<MessageEntity> messageEntity = messageRepository.findById(messageId);
        if (messageEntity.isEmpty()) {
            throw new EntityNotFoundException("The message with the given id was not found");
        }
        if (Boolean.FALSE.equals(messageEntity.get().getIsRead())) {
            throw new IllegalOperationException("Unread messages cannot be deleted");
        }
        messageRepository.deleteById(messageId);
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
    }

    private UserEntity validateAndGetUser(UserEntity userEntity) throws EntityNotFoundException {
        return userRepository.findById(userEntity.getId())
                .orElseThrow(() -> new EntityNotFoundException("The user with the given id was not found"));
    }
}
