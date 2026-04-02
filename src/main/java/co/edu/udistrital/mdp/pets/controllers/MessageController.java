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

import co.edu.udistrital.mdp.pets.dto.MessageDTO;
import co.edu.udistrital.mdp.pets.dto.MessageDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<MessageDTO> findAll() {
        return messageService.searchMessages().stream().map(MessageDTO::new).toList();
    }

    @GetMapping(value = "/{messageId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MessageDetailDTO findOne(@PathVariable Long messageId) throws EntityNotFoundException {
        return new MessageDetailDTO(messageService.searchMessage(messageId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public MessageDetailDTO create(@RequestBody MessageDTO messageDTO)
            throws EntityNotFoundException, IllegalOperationException {
        return new MessageDetailDTO(messageService.createMessage(messageDTO.toEntity()));
    }

    @PutMapping(value = "/{messageId}")
    @ResponseStatus(code = HttpStatus.OK)
    public MessageDetailDTO update(@PathVariable Long messageId, @RequestBody MessageDTO messageDTO)
            throws EntityNotFoundException, IllegalOperationException {
        return new MessageDetailDTO(messageService.updateMessage(messageId, messageDTO.toEntity()));
    }

    @DeleteMapping(value = "/{messageId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long messageId)
            throws EntityNotFoundException, IllegalOperationException {
        messageService.deleteMessage(messageId);
    }
}
