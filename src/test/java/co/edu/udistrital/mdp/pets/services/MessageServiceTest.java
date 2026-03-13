package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

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
import jakarta.transaction.Transactional;
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
            userEntity.setName("Message User " + i);
            userEntity.setPassword("Password123");
            userEntity.setPhone("30000000" + i);
            entityManager.persist(userEntity);
            userList.add(userEntity);
        }

        for (int i = 0; i < 3; i++) {
            MessageEntity messageEntity = factory.manufacturePojo(MessageEntity.class);
            messageEntity.setContent("Message " + i);
            messageEntity.setSender(userList.get(0));
            messageEntity.setReceiver(userList.get(1));
            messageEntity.setRead(Boolean.TRUE);
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
        newMessage.setRead(Boolean.FALSE);

        MessageEntity result = messageService.createMessage(newMessage);

        assertNotNull(result);
        MessageEntity stored = entityManager.find(MessageEntity.class, result.getId());
        assertEquals("Hello world", stored.getContent());
    }

    @Test
    void testCreateMessageWithoutContent() {
        MessageEntity newMessage = factory.manufacturePojo(MessageEntity.class);
        newMessage.setContent("");
        newMessage.setSender(userList.get(0));
        newMessage.setReceiver(userList.get(1));

        assertThrows(IllegalOperationException.class, () -> messageService.createMessage(newMessage));
    }

    @Test
    void testSearchMessage() throws Exception {
        MessageEntity found = messageService.searchMessage(messageList.get(0).getId());
        assertEquals(messageList.get(0).getId(), found.getId());
    }

    @Test
    void testSearchInvalidMessage() {
        assertThrows(EntityNotFoundException.class, () -> messageService.searchMessage(0L));
    }

    @Test
    void testSearchMessages() {
        List<MessageEntity> messages = messageService.searchMessages();
        assertEquals(messageList.size(), messages.size());
    }

    @Test
    void testUpdateMessage() throws Exception {
        MessageEntity newData = factory.manufacturePojo(MessageEntity.class);
        newData.setContent("Updated message");
        newData.setRead(Boolean.TRUE);

        MessageEntity updated = messageService.updateMessage(messageList.get(0).getId(), newData);

        assertEquals("Updated message", updated.getContent());
        assertTrue(updated.getRead());
    }

    @Test
    void testDeleteUnreadMessage() {
        MessageEntity unreadMessage = factory.manufacturePojo(MessageEntity.class);
        unreadMessage.setContent("Unread message");
        unreadMessage.setSender(userList.get(0));
        unreadMessage.setReceiver(userList.get(1));
        unreadMessage.setRead(Boolean.FALSE);
        entityManager.persist(unreadMessage);

        assertThrows(IllegalOperationException.class, () -> messageService.deleteMessage(unreadMessage.getId()));
    }

    @Test
    void testDeleteMessage() throws Exception {
        MessageEntity deletable = factory.manufacturePojo(MessageEntity.class);
        deletable.setContent("Delete message");
        deletable.setSender(userList.get(0));
        deletable.setReceiver(userList.get(1));
        deletable.setRead(Boolean.TRUE);
        entityManager.persist(deletable);

        messageService.deleteMessage(deletable.getId());
        MessageEntity deleted = entityManager.find(MessageEntity.class, deletable.getId());
        assertTrue(deleted == null);
    }
}

