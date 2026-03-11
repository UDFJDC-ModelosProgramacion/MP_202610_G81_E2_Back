package co.edu.udistrital.mdp.pets;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.MessageEntity;
import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
class MessageEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private MessageEntity messageEntity;

    @BeforeEach
    void setUp() {
        messageEntity = factory.manufacturePojo(MessageEntity.class);
    }

    @Test
    void testCreateMessage() {
        MessageEntity saved = entityManager.persistAndFlush(messageEntity);
        MessageEntity found = entityManager.find(MessageEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(messageEntity.getContent(), found.getContent());
        assertEquals(messageEntity.getSentAt(), found.getSentAt());
        assertEquals(messageEntity.getIsRead(), found.getIsRead());
    }

    @Test
    void testMessageWithSenderAndReceiver() {
        AdopterEntity sender = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persistAndFlush(sender);

        VeterinarianEntity receiver = factory.manufacturePojo(VeterinarianEntity.class);
        entityManager.persistAndFlush(receiver);

        messageEntity.setSender(sender);
        messageEntity.setReceiver(receiver);
        MessageEntity saved = entityManager.persistAndFlush(messageEntity);

        MessageEntity found = entityManager.find(MessageEntity.class, saved.getId());
        assertNotNull(found);
        assertNotNull(found.getSender());
        assertNotNull(found.getReceiver());
        assertEquals(sender.getId(), found.getSender().getId());
        assertEquals(receiver.getId(), found.getReceiver().getId());
    }
}
