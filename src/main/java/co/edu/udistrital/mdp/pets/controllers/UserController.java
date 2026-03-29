package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.UserDTO;
import co.edu.udistrital.mdp.pets.dto.UserDetailDTO;
import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.UserEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<UserDTO> findAll(@RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        List<UserEntity> users = (name != null || email != null)
                ? userService.searchUsers(name, email)
                : userService.searchUsers();
        return users.stream().map(UserDTO::new).toList();
    }

    @GetMapping(value = "/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserDetailDTO findOne(@PathVariable Long userId) throws EntityNotFoundException {
        return new UserDetailDTO(userService.searchUser(userId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public UserDetailDTO create(@RequestBody UserDTO userDTO)
            throws IllegalOperationException {
        //UserEntity es abstracta; en este proyecto el tipo concreto disponible dentro de este alcance es AdopterEntity. Por si acaso
        AdopterEntity entity = new AdopterEntity();
        userDTO.copyToEntity(entity);
        return new UserDetailDTO(userService.createUser(entity));
    }

    @PutMapping(value = "/{userId}")
    @ResponseStatus(code = HttpStatus.OK)
    public UserDetailDTO update(@PathVariable Long userId, @RequestBody UserDTO userDTO)
            throws EntityNotFoundException, IllegalOperationException {
        AdopterEntity entity = new AdopterEntity();
        userDTO.copyToEntity(entity);
        return new UserDetailDTO(userService.updateUser(userId, entity));
    }

    @DeleteMapping(value = "/{userId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long userId)
            throws EntityNotFoundException, IllegalOperationException {
        userService.deleteUser(userId);
    }
}
