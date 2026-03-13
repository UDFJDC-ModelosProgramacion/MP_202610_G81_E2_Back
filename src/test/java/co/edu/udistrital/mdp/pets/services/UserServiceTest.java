package co.edu.udistrital.mdp.pets.services;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import jakarta.transaction.Transactional;
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
        assertEquals(newUser.getEmail(), stored.getEmail());
    }

    @Test
    void testCreateUserWithDuplicateEmail() {
        AdopterEntity newUser = factory.manufacturePojo(AdopterEntity.class);
        newUser.setEmail(userList.get(0).getEmail());
        newUser.setPassword("Secure123");

        assertThrows(IllegalOperationException.class, () -> userService.createUser(newUser));
    }

    @Test
    void testSearchUser() throws Exception {
        UserEntity found = userService.searchUser(userList.get(0).getId());
        assertNotNull(found);
        assertEquals(userList.get(0).getEmail(), found.getEmail());
    }

    @Test
    void testSearchInvalidUser() {
        assertThrows(EntityNotFoundException.class, () -> userService.searchUser(0L));
    }

    @Test
    void testSearchUsers() {
        List<UserEntity> users = userService.searchUsers();
        assertEquals(userList.size(), users.size());
    }

    @Test
    void testUpdateUser() throws Exception {
        AdopterEntity newData = factory.manufacturePojo(AdopterEntity.class);
        newData.setEmail("updated@mail.com");
        newData.setPassword("Updated123");

        UserEntity updated = userService.updateUser(userList.get(0).getId(), newData);

        assertNotNull(updated);
        UserEntity stored = entityManager.find(AdopterEntity.class, userList.get(0).getId());
        assertEquals("updated@mail.com", stored.getEmail());
    }

    @Test
    void testDeleteUserWithAssociations() {
        AdopterEntity user = userList.get(0);
        NotificationEntity notification = factory.manufacturePojo(NotificationEntity.class);
        notification.setContent("Notification");
        notification.setUser(user);
        entityManager.persist(notification);

        assertThrows(IllegalOperationException.class, () -> userService.deleteUser(user.getId()));
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
        assertTrue(deleted == null);
    }
}
