package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.dto.AdopterDTO;
import co.edu.udistrital.mdp.pets.dto.MessageDTO;
import co.edu.udistrital.mdp.pets.dto.MessageDetailDTO;
import co.edu.udistrital.mdp.pets.dto.UserDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

@SpringBootTest
@Transactional
class MessageControllerTest {

    @Autowired
    private MessageController messageController;

    @Autowired
    private AdopterController adopterController;

    private MessageDTO messageDTO;
    private Long senderId;
    private Long receiverId;

    @BeforeEach
    void setUp() throws IllegalOperationException {
        AdopterDTO sender = new AdopterDTO();
        sender.setName("Sender");
        sender.setEmail("sender@test.com");
        sender.setPassword("password123");
        senderId = adopterController.create(sender).getId();

        AdopterDTO receiver = new AdopterDTO();
        receiver.setName("Receiver");
        receiver.setEmail("receiver@test.com");
        receiver.setPassword("password123");
        receiverId = adopterController.create(receiver).getId();

        messageDTO = new MessageDTO();
        messageDTO.setContent("Hello World");
        messageDTO.setIsRead(false);

        UserDTO senderDTO = new UserDTO();
        senderDTO.setId(senderId);
        messageDTO.setSender(senderDTO);

        UserDTO receiverDTO = new UserDTO();
        receiverDTO.setId(receiverId);
        messageDTO.setReceiver(receiverDTO);
    }

    @Test
    void testCreateMessage() throws IllegalOperationException, EntityNotFoundException {
        MessageDetailDTO createdMessage = messageController.create(messageDTO);

        assertNotNull(createdMessage);
        assertNotNull(createdMessage.getId());
        assertEquals(messageDTO.getContent(), createdMessage.getContent());
    }

    @Test
    void testFindAll() throws IllegalOperationException, EntityNotFoundException {
        messageController.create(messageDTO);

        List<MessageDTO> messages = messageController.findAll();
        assertNotNull(messages);
        assertFalse(messages.isEmpty());
    }

    @Test
    void testFindOne() throws IllegalOperationException, EntityNotFoundException {
        MessageDetailDTO createdMessage = messageController.create(messageDTO);

        MessageDetailDTO foundMessage = messageController.findOne(createdMessage.getId());
        assertNotNull(foundMessage);
        assertEquals(createdMessage.getId(), foundMessage.getId());
        assertEquals("Hello World", foundMessage.getContent());
    }

    @Test
    void testUpdate() throws IllegalOperationException, EntityNotFoundException {
        MessageDetailDTO createdMessage = messageController.create(messageDTO);

        messageDTO.setIsRead(true);
        MessageDetailDTO updatedMessage = messageController.update(createdMessage.getId(), messageDTO);

        assertNotNull(updatedMessage);
        assertTrue(updatedMessage.getIsRead());
    }

    @Test
    void testDelete() throws IllegalOperationException, EntityNotFoundException {
        MessageDetailDTO createdMessage = messageController.create(messageDTO);

        messageController.delete(createdMessage.getId());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            messageController.findOne(createdMessage.getId());
        });
        assertNotNull(exception);
    }
}
