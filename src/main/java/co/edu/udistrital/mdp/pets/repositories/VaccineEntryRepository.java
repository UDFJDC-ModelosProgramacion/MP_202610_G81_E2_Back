package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.VaccineEntryEntity;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineEntryRepository extends JpaRepository<VaccineEntryEntity, Long> {

          List<VaccineEntryEntity> findByMedicalHistoryId(Long medicalHistoryId);

          List<VaccineEntryEntity> findByNextDueDateBefore(LocalDate now);
}