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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.NotificationDTO;
import co.edu.udistrital.mdp.pets.dto.NotificationDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.NotificationService;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<NotificationDTO> findAll() {
        return notificationService.searchNotifications().stream().map(NotificationDTO::new).toList();
    }

    @GetMapping(value = "/{notificationId}")
    @ResponseStatus(code = HttpStatus.OK)
    public NotificationDetailDTO findOne(@PathVariable Long notificationId) throws EntityNotFoundException {
        return new NotificationDetailDTO(notificationService.searchNotification(notificationId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public NotificationDetailDTO create(@RequestBody NotificationDTO notificationDTO)
            throws EntityNotFoundException, IllegalOperationException {
        return new NotificationDetailDTO(notificationService.createNotification(notificationDTO.toEntity()));
    }

    @PutMapping(value = "/{notificationId}")
    @ResponseStatus(code = HttpStatus.OK)
    public NotificationDetailDTO update(@PathVariable Long notificationId, @RequestBody NotificationDTO notificationDTO)
            throws EntityNotFoundException, IllegalOperationException {
        return new NotificationDetailDTO(notificationService.updateNotification(notificationId, notificationDTO.toEntity()));
    }

    @DeleteMapping(value = "/{notificationId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long notificationId)
            throws EntityNotFoundException, IllegalOperationException {
        notificationService.deleteNotification(notificationId);
    }
}
