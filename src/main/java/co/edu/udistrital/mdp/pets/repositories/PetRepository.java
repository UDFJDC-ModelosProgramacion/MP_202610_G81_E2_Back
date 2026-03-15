package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long> {
    List<PetEntity> findByShelterId(Long shelterId);

    boolean existsByNameAndBreedAndBornDate(String name, String breed, LocalDate bornDate);

    List<PetEntity> searchPets(String breed, String size, String temperament);
}
