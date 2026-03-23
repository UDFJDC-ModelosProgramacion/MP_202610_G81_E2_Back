package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;




@RestController
@RequestMapping("/users")
public class UserController {
    
    private List<Map<String, Object>> users = new ArrayList<>();
    private Map<Long, List<Map<String, Object>>> userPets = new HashMap<>();

    @PostMapping
    public Map<String, Object> createUser(@RequestBody Map<String, Object> user){
        user.put("id", users.size() + 1);
        users.add(user);
        return user;
    }

    @GetMapping
    public List<Map<String, Object>> getUsers(){
        return users;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getUserById(@PathVariable Long id) {
        for (Map<String, Object> user : users){
            Object userId = user.get("id");

            if(userId != null && Long.valueOf(userId.toString()).equals(id)){
                return user;
            }
        }
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }

    @GetMapping("/{id}/pets")
    public List<Map<String, Object>> getUserPets(@PathVariable Long id) {
        return userPets.getOrDefault(id, new ArrayList<>());
    }
    
    @PostMapping("/{id}/pets")
    public List<Map<String, Object>> addPetToUser(@PathVariable Long id,@RequestBody Map<String, Object> pet) {
        
        pet.put("id", new Random().nextInt(1000));

        userPets.putIfAbsent(id, new ArrayList<>());
        userPets.get(id).add(pet);
        
        return userPets.get(id);
    }
    
    @GetMapping("/{id}/notifications")
    public List<Map<String, Object>> getUserNotifications(@PathVariable Long id) {
        return new ArrayList<>();
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id){
        boolean removed = users.removeIf(user -> 
            Long.valueOf(user.get("id").toString()).equals(id)
        );

        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
    
    @PutMapping("/{id}")
    public Map<String, Object> updateUser(@PathVariable Long id,
            @RequestBody Map<String, Object> updatedUser){

        for (Map<String, Object> user : users) {
            Long userId = Long.valueOf(user.get("id").toString());

            if (userId.equals(id)) {
                user.putAll(updatedUser);
                user.put("id", id); // evitar que cambien el id
                return user;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    }
}