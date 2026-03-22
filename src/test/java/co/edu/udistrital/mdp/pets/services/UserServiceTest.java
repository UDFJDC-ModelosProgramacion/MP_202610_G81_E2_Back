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
import co.edu.udistrital.mdp.pets.entities.NotificationEntity;
import co.edu.udistrital.mdp.pets.entities.UserEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import org.springframework.transaction.annotation.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
@Transactional
@Import(UserService.class)
class UserServiceTest {

    @Autowired
    private UserService userService;

    @Autowired
    private TestEntityManager entityManager;

    private final PodamFactory factory = new PodamFactoryImpl();
    private final List<AdopterEntity> userList = new ArrayList<>();

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
            userEntity.setEmail("user" + i + "@mail.com");
            userEntity.setPassword("Password123");
            entityManager.persist(userEntity);
            userList.add(userEntity);
        }
    }

    @Test
    void testCreateUser() throws Exception {
        AdopterEntity newUser = factory.manufacturePojo(AdopterEntity.class);
        newUser.setEmail("newuser@mail.com");
        newUser.setPassword("Secure123");

        UserEntity result = userService.createUser(newUser);

        assertNotNull(result);
        UserEntity stored = entityManager.find(AdopterEntity.class, result.getId());
        assertEquals(newUser.getId(), stored.getId());
        assertEquals(newUser.getEmail(), stored.getEmail());
        assertEquals(newUser.getPassword(), stored.getPassword());
        assertEquals(newUser.getPhoneNumber(), stored.getPhoneNumber());
    }

    @Test
    void testCreateUserNull() throws Exception {
        assertNotNull(assertThrows(IllegalOperationException.class, () -> userService.createUser(null)));
    }

    @Test
    void testCreateUserWithInvalidEmail() throws Exception {
        assertNotNull(assertThrows(IllegalOperationException.class, () -> {
            AdopterEntity newUser = factory.manufacturePojo(AdopterEntity.class);
            newUser.setName("Valid Name");
            newUser.setEmail("");
            newUser.setPassword("Secure123");
            userService.createUser(newUser);
        }));
    }

    @Test
    void testCreateUserWithInvalidName() throws Exception {
        assertNotNull(assertThrows(IllegalOperationException.class, () -> {
            AdopterEntity newUser = factory.manufacturePojo(AdopterEntity.class);
            newUser.setName("");
            newUser.setEmail("valid@mail.com");
            newUser.setPassword("Secure123");
            userService.createUser(newUser);
        }));
    }

    @Test
    void testCreateUserWithInvalidPassword() throws Exception {
        assertNotNull(assertThrows(IllegalOperationException.class, () -> {
            AdopterEntity newUser = factory.manufacturePojo(AdopterEntity.class);
            newUser.setEmail("valid@mail.com");
            newUser.setPassword("");
            userService.createUser(newUser);
        }));
    }

    @Test
    void testCreateUserWithDuplicateEmail() throws Exception {
        AdopterEntity newUser = factory.manufacturePojo(AdopterEntity.class);
        newUser.setEmail(userList.get(0).getEmail());
        newUser.setPassword("Secure123");

        assertNotNull(assertThrows(IllegalOperationException.class, () -> userService.createUser(newUser)));
    }

    @Test
    void testSearchUser() throws Exception {
        UserEntity entity = userList.get(0);
        UserEntity resultEntity = userService.searchUser(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getEmail(), resultEntity.getEmail());
        assertEquals(entity.getPassword(), resultEntity.getPassword());
        assertEquals(entity.getPhoneNumber(), resultEntity.getPhoneNumber());
    }

    @Test
    void testSearchInvalidUser() throws Exception {
        assertNotNull(assertThrows(EntityNotFoundException.class, () -> userService.searchUser(0L)));
    }

    @Test
    void testSearchUsers() throws Exception {
        List<UserEntity> users = userService.searchUsers();
        assertEquals(userList.size(), users.size());
        for (UserEntity entity : users) {
            boolean found = false;
            for (UserEntity storedEntity : userList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testSearchUsersWithFilters() throws Exception {
        List<UserEntity> usersByName = userService.searchUsers(userList.get(0).getName(), null);
        assertTrue(usersByName.size() > 0);
        
        List<UserEntity> usersByEmail = userService.searchUsers(null, userList.get(0).getEmail());
        assertTrue(usersByEmail.size() > 0);
    }

    @Test
    void testUpdateUser() throws Exception {
        AdopterEntity pojoEntity = factory.manufacturePojo(AdopterEntity.class);
        pojoEntity.setEmail("updated@mail.com");
        pojoEntity.setPassword("Updated123");

        userService.updateUser(userList.get(0).getId(), pojoEntity);

        UserEntity stored = entityManager.find(AdopterEntity.class, userList.get(0).getId());
        assertEquals(pojoEntity.getEmail(), stored.getEmail());
        assertEquals(pojoEntity.getPassword(), stored.getPassword());
        assertEquals(pojoEntity.getPhoneNumber(), stored.getPhoneNumber());
    }

    @Test
    void testUpdateUserNotFound() throws Exception {
        assertNotNull(assertThrows(EntityNotFoundException.class, () -> {
            AdopterEntity pojoEntity = factory.manufacturePojo(AdopterEntity.class);
            pojoEntity.setEmail("updated@mail.com");
            pojoEntity.setPassword("Updated123");
            userService.updateUser(0L, pojoEntity);
        }));
    }

    @Test
    void testUpdateUserDuplicateEmail() throws Exception {
        AdopterEntity pojoEntity = factory.manufacturePojo(AdopterEntity.class);
        pojoEntity.setEmail(userList.get(1).getEmail());
        assertNotNull(assertThrows(IllegalOperationException.class, () -> {
            userService.updateUser(userList.get(0).getId(), pojoEntity);
        }));
    }

    @Test
    void testDeleteUserWithSentMessages() throws Exception {
        AdopterEntity user = userList.get(0);
        co.edu.udistrital.mdp.pets.entities.MessageEntity msg = factory.manufacturePojo(co.edu.udistrital.mdp.pets.entities.MessageEntity.class);
        msg.setSender(user);
        msg.setReceiver(user);
        entityManager.persist(msg);
        assertNotNull(assertThrows(IllegalOperationException.class, () -> userService.deleteUser(user.getId())));
    }

    @Test
    void testDeleteUserWithReceivedMessages() throws Exception {
        AdopterEntity user = userList.get(0);
        co.edu.udistrital.mdp.pets.entities.MessageEntity msg = factory.manufacturePojo(co.edu.udistrital.mdp.pets.entities.MessageEntity.class);
        msg.setSender(userList.get(1));
        msg.setReceiver(user);
        entityManager.persist(msg);
        user.getReceivedMessages().add(msg);
        assertNotNull(assertThrows(IllegalOperationException.class, () -> userService.deleteUser(user.getId())));
    }

    @Test
    void testDeleteUserWithAssociations() throws Exception {
        AdopterEntity user = userList.get(0);
        NotificationEntity notification = factory.manufacturePojo(NotificationEntity.class);
        notification.setContent("Notification");
        notification.setUser(user);
        entityManager.persist(notification);

        assertNotNull(assertThrows(IllegalOperationException.class, () -> userService.deleteUser(user.getId())));
    }

    @Test
    void testDeleteUser() throws Exception {
        AdopterEntity user = factory.manufacturePojo(AdopterEntity.class);
        user.setEmail("delete@mail.com");
        user.setPassword("Secure123");
        user.setSentMessages(new ArrayList<>());
        user.setReceivedMessages(new ArrayList<>());
        user.setNotifications(new ArrayList<>());
        entityManager.persist(user);

        userService.deleteUser(user.getId());
        UserEntity deleted = entityManager.find(AdopterEntity.class, user.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteInvalidUser() throws Exception {
        assertNotNull(assertThrows(EntityNotFoundException.class, () -> userService.deleteUser(0L)));
    }
}
