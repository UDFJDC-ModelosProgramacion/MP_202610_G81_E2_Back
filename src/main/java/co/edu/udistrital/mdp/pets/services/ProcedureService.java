package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ProcedureEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ProcedureRepository;

@Service
public class ProcedureService {

    @Autowired
    private ProcedureRepository procedureRepository;

    @Transactional
    public ProcedureEntity createProcedure(ProcedureEntity procedure) throws IllegalOperationException {
        if (procedure == null) throw new IllegalOperationException("Procedure cannot be null");
        return procedureRepository.save(procedure);
    }

    @Transactional
    public ProcedureEntity searchProcedure(Long id) throws EntityNotFoundException {
        Objects.requireNonNull(id, "Id cannot be null");
        Optional<ProcedureEntity> entity = procedureRepository.findById(id);
        if (entity.isEmpty()) throw new EntityNotFoundException("Procedure not found");
        return entity.get();
    }

    @Transactional
    public List<ProcedureEntity> searchProcedures() {
        return procedureRepository.findAll();
    }

    @Transactional
    public ProcedureEntity updateProcedure(Long id, ProcedureEntity procedure) throws EntityNotFoundException, IllegalOperationException {
        Objects.requireNonNull(id, "Id cannot be null");
        Optional<ProcedureEntity> entity = procedureRepository.findById(id);
        if (entity.isEmpty()) throw new EntityNotFoundException("Procedure not found");

        ProcedureEntity existing = entity.get();
        existing.setProcedureName(procedure.getProcedureName());
        existing.setDescription(procedure.getDescription());
        existing.setProcedureDate(procedure.getProcedureDate());
        existing.setOutcome(procedure.getOutcome());
        
        return procedureRepository.save(existing);
    }

    @Transactional
    public void deleteProcedure(Long id) throws EntityNotFoundException {
        Objects.requireNonNull(id, "Id cannot be null");
        Optional<ProcedureEntity> entity = procedureRepository.findById(id);
        if (entity.isEmpty()) throw new EntityNotFoundException("Procedure not found");
        procedureRepository.delete(entity.get());
    }
}
