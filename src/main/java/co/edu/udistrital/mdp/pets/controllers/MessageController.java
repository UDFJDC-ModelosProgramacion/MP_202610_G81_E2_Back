package co.edu.udistrital.mdp.pets.controllers;

import java.util.*;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private List<Map<String, Object>> messages = new ArrayList<>();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> sendMessage(@RequestBody Map<String, Object> msg){

        if (!msg.containsKey("senderId") || !msg.containsKey("receiverId") || !msg.containsKey("content")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing fields");
        }

        msg.put("id", messages.size() + 1);
        msg.put("sentAt", java.time.LocalDateTime.now());
        msg.put("isRead", false);
        msg.put("active", true);

        messages.add(msg);
        return msg;
    }

    @GetMapping
    public List<Map<String, Object>> getMessages(@RequestParam(required = false) Long userId){

        if (userId == null) return messages;

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> m : messages) {
            Long sender = Long.valueOf(m.get("senderId").toString());
            Long receiver = Long.valueOf(m.get("receiverId").toString());

            if (sender.equals(userId) || receiver.equals(userId)) {
                result.add(m);
            }
        }

        return result;
    }

    @GetMapping("/sent")
    public List<Map<String, Object>> getSentMessages(@RequestParam Long userId){

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> m : messages) {
            Long sender = Long.valueOf(m.get("senderId").toString());

            if (sender.equals(userId)) {
                result.add(m);
            }
        }

        return result;
    }

    @GetMapping("/conversation")
    public List<Map<String, Object>> getConversation(@RequestParam Long user1, @RequestParam Long user2){

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> m : messages) {
            Long sender = Long.valueOf(m.get("senderId").toString());
            Long receiver = Long.valueOf(m.get("receiverId").toString());

            boolean match = (sender.equals(user1) && receiver.equals(user2)) ||
                            (sender.equals(user2) && receiver.equals(user1));

            if (match && (Boolean) m.get("active")) {
                result.add(m);
            }
        }

        return result;
    }

    @GetMapping("/received")
    public List<Map<String, Object>> getReceivedMessages(@RequestParam Long userId){

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> m : messages) {
            Long receiver = Long.valueOf(m.get("receiverId").toString());

            if (receiver.equals(userId)) {
                result.add(m);
            }
        }

        return result;
    }

    @PatchMapping("/{id}")
    public Map<String, Object> markAsRead(@PathVariable Long id){

        for (Map<String, Object> m : messages) {
            Long mid = Long.valueOf(m.get("id").toString());

            if (mid.equals(id)) {
                m.put("isRead", true);
                return m;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMessage(@PathVariable Long id){

        for (Map<String, Object> m : messages) {
            Long mid = Long.valueOf(m.get("id").toString());

            if (mid.equals(id)) {
                m.put("active", false);
                return;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Message not found");
    }
}