package co.edu.udistrital.mdp.pets.controllers;

import java.util.*;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/notifications")
public class NotificationsController {
    private List<Map<String, Object>> notifications = new ArrayList<>();

    @PostMapping
    public Map<String, Object> createNotification(@RequestBody Map<String, Object> notification) {
        notification.put("id", notifications.size() + 1);
        notification.put("createdAt", java.time.LocalDateTime.now());
        notification.put("isRead", false);

        if(!notification.containsKey("userId")){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "userId is required");
        }

        notifications.add(notification);
        return notification;
    }
    
    @GetMapping
    public List<Map<String, Object>> getNotifications(@RequestParam(required = false) Long userId) {
        
        if (userId == null){
            return notifications;
        }

        List<Map<String, Object>> result = new ArrayList<>();

        for(Map<String, Object> n : notifications){
            Long uid = Long.valueOf(n.get("userId").toString());

            if(uid.equals(userId)){
                result.add(n);
            }
        }
        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getNotificationById(@PathVariable Long id) {
        return notifications.stream().filter(n -> Long.valueOf(n.get("id").toString())
        .equals(id)).findFirst()
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found"));
    }
    
    @PatchMapping("/{id}")
    public Map<String, Object> markAsRead(@PathVariable Long id, @RequestBody Map<String, Object> body){
        for(Map<String, Object> n : notifications){
            Long nid = Long.valueOf(n.get("id").toString());

            if(nid.equals(id)){
                n.put("isRead", true);
                return n;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotification(@PathVariable Long id){
        boolean removed = notifications.removeIf(n -> Long.valueOf(n.get("id").toString()).equals(id));

        if (!removed){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Notification not found");
        }
    }
}
