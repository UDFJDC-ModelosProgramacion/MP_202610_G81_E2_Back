package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ReturnCaseEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ReturnCaseRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReturnCaseService {
    private static final String NOT_FOUND_MSG = "Return case not found";

    private static final String RC_NOT_FOUND = NOT_FOUND_MSG;

    @Autowired
    private ReturnCaseRepository returnCaseRepository;

    @Transactional
    public ReturnCaseEntity createReturnCase(ReturnCaseEntity returnCase) throws IllegalOperationException {
        log.info("Inicia proceso de creación del caso de devolución");

        if (returnCase == null) {
            throw new IllegalOperationException("Return case cannot be null");
        }

        log.info("Termina proceso de creación del caso de devolución");
        return returnCaseRepository.save(returnCase);
    }

    @Transactional
    public ReturnCaseEntity searchReturnCase(Long id) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar el caso de devolución con id = {}", id);
        Objects.requireNonNull(id, "Return case id cannot be null");

        Optional<ReturnCaseEntity> returnCaseEntity = returnCaseRepository.findById(id);
        if (returnCaseEntity.isEmpty())
            throw new EntityNotFoundException(RC_NOT_FOUND);

        log.info("Termina proceso de consultar el caso de devolución con id = {}", id);
        return returnCaseEntity.get();
    }

    @Transactional
    public List<ReturnCaseEntity> searchReturnCases() {
        log.info("Inicia proceso de consultar todos los casos de devolución");
        return returnCaseRepository.findAll();
    }

    @Transactional
    public ReturnCaseEntity updateReturnCase(Long id, ReturnCaseEntity returnCase)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar el caso de devolución con id = {}", id);
        Objects.requireNonNull(id, "Return case id cannot be null");

        Optional<ReturnCaseEntity> returnCaseEntity = returnCaseRepository.findById(id);
        if (returnCaseEntity.isEmpty())
            throw new EntityNotFoundException(RC_NOT_FOUND);

        ReturnCaseEntity existing = returnCaseEntity.get();
        existing.setReturnDate(returnCase.getReturnDate());
        existing.setReason(returnCase.getReason());
        existing.setDetails(returnCase.getDetails());

        log.info("Termina proceso de actualizar el caso de devolución con id = {}", id);
        return returnCaseRepository.save(existing);
    }

    @Transactional
    public void deleteReturnCase(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar el caso de devolución con id = {}", id);
        Objects.requireNonNull(id, "Return case id cannot be null");

        Optional<ReturnCaseEntity> returnCaseEntity = returnCaseRepository.findById(id);
        if (returnCaseEntity.isEmpty())
            throw new EntityNotFoundException(RC_NOT_FOUND);

        ReturnCaseEntity returnCase = returnCaseEntity.get();

        if (returnCase.getAdoptionProcess() != null) {
            throw new IllegalOperationException("Cannot delete return case associated to an adoption process");
        }

        returnCaseRepository.delete(returnCase);
        log.info("Termina proceso de borrar el caso de devolución con id = {}", id);
    }
}