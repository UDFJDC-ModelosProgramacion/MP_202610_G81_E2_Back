package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.VetSpecialityEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VetSpecialityRepository extends JpaRepository<VetSpecialityEntity, Long> {
    Optional<VetSpecialityEntity> findByNameIgnoreCase(String name);
    List<VetSpecialityEntity> findByNameContainingIgnoreCase(String name);
}
