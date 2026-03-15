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
import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import org.springframework.transaction.annotation.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(MessageService.class)
class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<AdopterEntity> userList = new ArrayList<>();
    private final List<MessageEntity> messageList = new ArrayList<>();

    @BeforeEach
    @SuppressWarnings("unused")
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
            userEntity.setEmail("messageuser" + i + "@mail.com");
            userEntity.setPassword("Password123");
            userEntity.setPhoneNumber("30000000" + i);
            entityManager.persist(userEntity);
            userList.add(userEntity);
        }

        for (int i = 0; i < 3; i++) {
            MessageEntity messageEntity = factory.manufacturePojo(MessageEntity.class);
            messageEntity.setContent("Message " + i);
            messageEntity.setSender(userList.get(0));
            messageEntity.setReceiver(userList.get(1));
            messageEntity.setIsRead(Boolean.TRUE);
            entityManager.persist(messageEntity);
            messageList.add(messageEntity);
        }
    }

    @Test
    void testCreateMessage() throws Exception {
        MessageEntity newMessage = factory.manufacturePojo(MessageEntity.class);
        newMessage.setContent("Hello world");
        newMessage.setSender(userList.get(0));
        newMessage.setReceiver(userList.get(1));
        newMessage.setIsRead(Boolean.FALSE);

        MessageEntity result = messageService.createMessage(newMessage);

        assertNotNull(result);
        MessageEntity stored = entityManager.find(MessageEntity.class, result.getId());
        assertEquals(newMessage.getContent(), stored.getContent());
        assertEquals(newMessage.getIsRead(), stored.getIsRead());
        assertEquals(userList.get(0).getId(), stored.getSender().getId());
        assertEquals(userList.get(1).getId(), stored.getReceiver().getId());
    }

    @Test
    void testCreateMessageNull() {
        assertNotNull(assertThrows(IllegalOperationException.class, () -> messageService.createMessage(null)));
    }

    @Test
    void testCreateMessageWithNullSender() {
        assertNotNull(assertThrows(IllegalOperationException.class, () -> {
            MessageEntity newMessage = factory.manufacturePojo(MessageEntity.class);
            newMessage.setContent("Hello");
            newMessage.setSender(null);
            newMessage.setReceiver(userList.get(1));
            messageService.createMessage(newMessage);
        }));
    }

    @Test
    void testCreateMessageWithNullReceiver() {
        assertNotNull(assertThrows(IllegalOperationException.class, () -> {
            MessageEntity newMessage = factory.manufacturePojo(MessageEntity.class);
            newMessage.setContent("Hello");
            newMessage.setSender(userList.get(0));
            newMessage.setReceiver(null);
            messageService.createMessage(newMessage);
        }));
    }

    @Test
    void testCreateMessageWithInvalidSender() {
        assertNotNull(assertThrows(EntityNotFoundException.class, () -> {
            MessageEntity newMessage = factory.manufacturePojo(MessageEntity.class);
            newMessage.setContent("Hello");
            AdopterEntity invalidUser = new AdopterEntity();
            invalidUser.setId(0L);
            newMessage.setSender(invalidUser);
            newMessage.setReceiver(userList.get(1));
            messageService.createMessage(newMessage);
        }));
    }

    @Test
    void testCreateMessageWithoutContent() {
        MessageEntity newMessage = factory.manufacturePojo(MessageEntity.class);
        newMessage.setContent("");
        newMessage.setSender(userList.get(0));
        newMessage.setReceiver(userList.get(1));

        assertNotNull(assertThrows(IllegalOperationException.class, () -> messageService.createMessage(newMessage)));
    }

    @Test
    void testSearchMessage() throws Exception {
        MessageEntity entity = messageList.get(0);
        MessageEntity resultEntity = messageService.searchMessage(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getContent(), resultEntity.getContent());
        assertEquals(entity.getIsRead(), resultEntity.getIsRead());
    }

    @Test
    void testSearchInvalidMessage() {
        assertNotNull(assertThrows(EntityNotFoundException.class, () -> messageService.searchMessage(0L)));
    }

    @Test
    void testSearchMessages() {
        List<MessageEntity> messages = messageService.searchMessages();
        assertEquals(messageList.size(), messages.size());
        for (MessageEntity entity : messages) {
            boolean found = false;
            for (MessageEntity storedEntity : messageList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testUpdateMessage() throws Exception {
        MessageEntity newData = factory.manufacturePojo(MessageEntity.class);
        newData.setContent("Updated message");
        newData.setIsRead(Boolean.TRUE);

        messageService.updateMessage(messageList.get(0).getId(), newData);

        MessageEntity stored = entityManager.find(MessageEntity.class, messageList.get(0).getId());
        assertEquals("Updated message", stored.getContent());
        assertEquals(Boolean.TRUE, stored.getIsRead());
    }

    @Test
    void testUpdateMessageNotFound() {
        assertNotNull(assertThrows(EntityNotFoundException.class, () -> {
            MessageEntity newData = factory.manufacturePojo(MessageEntity.class);
            newData.setContent("Updated message");
            messageService.updateMessage(0L, newData);
        }));
    }

    @Test
    void testDeleteUnreadMessage() {
        MessageEntity unreadMessage = factory.manufacturePojo(MessageEntity.class);
        unreadMessage.setContent("Unread message");
        unreadMessage.setSender(userList.get(0));
        unreadMessage.setReceiver(userList.get(1));
        unreadMessage.setIsRead(Boolean.FALSE);
        entityManager.persist(unreadMessage);

        assertNotNull(assertThrows(IllegalOperationException.class, () -> messageService.deleteMessage(unreadMessage.getId())));
    }

    @Test
    void testDeleteMessage() throws Exception {
        MessageEntity deletable = factory.manufacturePojo(MessageEntity.class);
        deletable.setContent("Delete message");
        deletable.setSender(userList.get(0));
        deletable.setReceiver(userList.get(1));
        deletable.setIsRead(Boolean.TRUE);
        entityManager.persist(deletable);

        messageService.deleteMessage(deletable.getId());
        MessageEntity deleted = entityManager.find(MessageEntity.class, deletable.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteInvalidMessage() {
        assertNotNull(assertThrows(EntityNotFoundException.class, () -> messageService.deleteMessage(0L)));
    }
}

