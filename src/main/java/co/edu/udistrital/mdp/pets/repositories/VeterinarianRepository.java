package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.VeterinarianEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeterinarianRepository extends JpaRepository<VeterinarianEntity, Long> {
}
