package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.UserEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public UserEntity createUser(UserEntity userEntity) throws IllegalOperationException {
        log.info("Inicia proceso de creación del usuario");
        validateUserData(userEntity);
        if (!userRepository.findByEmail(userEntity.getEmail()).isEmpty()) {
            throw new IllegalOperationException("Email already exists");
        }
        log.info("Termina proceso de creación del usuario");
        return userRepository.save(userEntity);
    }

    @Transactional
    public UserEntity searchUser(Long userId) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar el usuario con id = {}", userId);
        Long safeUserId = Objects.requireNonNull(userId, "userId must not be null");
        Optional<UserEntity> userEntity = userRepository.findById(safeUserId);
        if (userEntity.isEmpty()) {
            throw new EntityNotFoundException("The user with the given id was not found");
        }
        log.info("Termina proceso de consultar el usuario con id = {}", userId);
        return userEntity.get();
    }

    @Transactional
    public List<UserEntity> searchUsers() {
        log.info("Inicia proceso de consultar todos los usuarios");
        return userRepository.findAll();
    }

    @Transactional
    public UserEntity updateUser(Long userId, UserEntity userEntity)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar el usuario con id = {}", userId);
        Long safeUserId = Objects.requireNonNull(userId, "userId must not be null");
        Optional<UserEntity> persistedUser = userRepository.findById(safeUserId);
        if (persistedUser.isEmpty()) {
            throw new EntityNotFoundException("The user with the given id was not found");
        }
        validateUserData(userEntity);

        List<UserEntity> usersWithSameEmail = userRepository.findByEmail(userEntity.getEmail());
        boolean repeatedEmail = usersWithSameEmail.stream().anyMatch(user -> !user.getId().equals(userId));
        if (repeatedEmail) {
            throw new IllegalOperationException("Email already exists");
        }

        userEntity.setId(userId);
        userEntity.setSentMessages(persistedUser.get().getSentMessages());
        userEntity.setReceivedMessages(persistedUser.get().getReceivedMessages());
        userEntity.setNotifications(persistedUser.get().getNotifications());
        log.info("Termina proceso de actualizar el usuario con id = {}", userId);
        return userRepository.save(userEntity);
    }

    @Transactional
    public void deleteUser(Long userId) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar el usuario con id = {}", userId);
        Long safeUserId = Objects.requireNonNull(userId, "userId must not be null");
        Optional<UserEntity> userEntity = userRepository.findById(safeUserId);
        if (userEntity.isEmpty()) {
            throw new EntityNotFoundException("The user with the given id was not found");
        }
        UserEntity user = userEntity.get();
        if (!user.getSentMessages().isEmpty() || !user.getReceivedMessages().isEmpty() || !user.getNotifications().isEmpty()) {
            throw new IllegalOperationException("The user has active associations and cannot be deleted");
        }
        userRepository.deleteById(safeUserId);
        log.info("Termina proceso de borrar el usuario con id = {}", userId);
    }

    private void validateUserData(UserEntity userEntity) throws IllegalOperationException {
        if (userEntity == null) {
            throw new IllegalOperationException("User is not valid");
        }
        if (userEntity.getEmail() == null || userEntity.getEmail().isBlank()) {
            throw new IllegalOperationException("User email is not valid");
        }
        if (userEntity.getPassword() == null || userEntity.getPassword().isBlank()) {
            throw new IllegalOperationException("User password is not valid");
        }
    }
}
