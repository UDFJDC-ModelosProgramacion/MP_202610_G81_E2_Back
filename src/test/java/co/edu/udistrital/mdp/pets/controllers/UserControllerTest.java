package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.dto.UserDTO;
import co.edu.udistrital.mdp.pets.dto.UserDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

@SpringBootTest
@Transactional
class UserControllerTest {

    @Autowired
    private UserController userController;

    private UserDTO userDTO;

    @SuppressWarnings("java:S1144")
    void initializeTestData() {
        userDTO = new UserDTO();
        userDTO.setName("Test User");
        userDTO.setEmail("test@user.com");
        userDTO.setPassword("password123");
        userDTO.setPhoneNumber("1234567890");
    }

    @Test
    void testCreateUser() throws IllegalOperationException {
        initializeTestData();
        UserDetailDTO createdUser = userController.create(userDTO);

        assertNotNull(createdUser);
        assertNotNull(createdUser.getId());
        assertEquals(userDTO.getName(), createdUser.getName());
        assertEquals(userDTO.getEmail(), createdUser.getEmail());
    }

    @Test
    void testFindAll() throws IllegalOperationException {
        initializeTestData();
        userController.create(userDTO);

        List<UserDTO> users = userController.findAll(null, null);
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    void testFindOne() throws IllegalOperationException, EntityNotFoundException {
        initializeTestData();
        UserDetailDTO createdUser = userController.create(userDTO);

        UserDetailDTO foundUser = userController.findOne(createdUser.getId());
        assertNotNull(foundUser);
        assertEquals(createdUser.getId(), foundUser.getId());
        assertEquals("Test User", foundUser.getName());
    }

    @Test
    void testUpdate() throws IllegalOperationException, EntityNotFoundException {
        initializeTestData();
        UserDetailDTO createdUser = userController.create(userDTO);

        userDTO.setName("Updated Name");
        UserDetailDTO updatedUser = userController.update(createdUser.getId(), userDTO);

        assertNotNull(updatedUser);
        assertEquals("Updated Name", updatedUser.getName());
    }

    @Test
    void testDelete() throws IllegalOperationException, EntityNotFoundException {
        initializeTestData();
        UserDetailDTO createdUser = userController.create(userDTO);

        userController.delete(createdUser.getId());

        Exception exception = assertThrows(EntityNotFoundException.class, () -> {
            userController.findOne(createdUser.getId());
        });
        assertNotNull(exception);
    }
}
