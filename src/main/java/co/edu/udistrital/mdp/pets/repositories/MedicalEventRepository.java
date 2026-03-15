package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.MedicalEventEntity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicalEventRepository extends JpaRepository<MedicalEventEntity, Long> {

          List<MedicalEventEntity> findByEventDateBetween(LocalDate startDate, LocalDate endDate);
}
