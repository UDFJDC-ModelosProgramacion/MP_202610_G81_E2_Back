package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ProcedureEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ProcedureRepository;

@Service
public class ProcedureService {
    private static final String ID_NULL_MSG = "Id cannot be null";

    private final ProcedureRepository procedureRepository;

    public ProcedureService(ProcedureRepository procedureRepository) {
        this.procedureRepository = procedureRepository;
    }

    @Transactional
    public ProcedureEntity createProcedure(ProcedureEntity procedure) throws IllegalOperationException {
        if (procedure == null) throw new IllegalOperationException("Procedure cannot be null");
        return procedureRepository.save(procedure);
    }

    @Transactional
    public ProcedureEntity searchProcedure(Long id) throws EntityNotFoundException {
        Long safeId = Objects.requireNonNull(id, ID_NULL_MSG);
        Optional<ProcedureEntity> entity = procedureRepository.findById(safeId);
        if (entity.isEmpty()) throw new EntityNotFoundException("Procedure not found");
        return entity.get();
    }

    @Transactional
    public List<ProcedureEntity> searchProcedures() {
        return procedureRepository.findAll();
    }

    @Transactional
    public ProcedureEntity updateProcedure(Long id, ProcedureEntity procedure) throws EntityNotFoundException {
        Long safeId = Objects.requireNonNull(id, ID_NULL_MSG);
        Optional<ProcedureEntity> entity = procedureRepository.findById(safeId);
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
        Long safeId = Objects.requireNonNull(id, ID_NULL_MSG);
        Optional<ProcedureEntity> entity = procedureRepository.findById(safeId);
        if (entity.isEmpty()) throw new EntityNotFoundException("Procedure not found");
        procedureRepository.deleteById(safeId);
    }
}
