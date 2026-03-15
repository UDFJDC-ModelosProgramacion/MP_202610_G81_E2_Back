package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PetRepository extends JpaRepository<PetEntity, Long> {
    List<PetEntity> findByShelterId(Long shelterId);

    boolean existsByNameAndBreedAndBornDate(String name, String breed, LocalDate bornDate);

    @Query("SELECT p FROM PetEntity p WHERE " +
           "(:breed IS NULL OR p.breed = :breed) AND " +
           "(:size IS NULL OR p.size = :size) AND " +
           "(:temperament IS NULL OR p.temperament = :temperament)")
    List<PetEntity> searchPets(@Param("breed") String breed, @Param("size") String size, @Param("temperament") String temperament);
}

