package co.edu.udistrital.mdp.pets.repositories;

import co.edu.udistrital.mdp.pets.entities.ShelterMediaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShelterMediaRepository extends JpaRepository<ShelterMediaEntity, Long> {
    List<ShelterMediaEntity> findByShelterId(Long shelterId);
    int countByShelterIdAndMediaType(Long shelterId, String mediaType);
}
