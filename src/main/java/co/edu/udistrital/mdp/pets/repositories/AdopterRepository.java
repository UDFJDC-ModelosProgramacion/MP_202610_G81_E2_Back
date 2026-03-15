package co.edu.udistrital.mdp.pets.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface AdopterRepository extends JpaRepository<AdopterEntity, Long> {
	List<AdopterEntity> findByEmail(String email);

	@Query("SELECT a FROM AdopterEntity a WHERE " +
	       "(:name IS NULL OR :name = '' OR LOWER(a.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
	       "(:email IS NULL OR :email = '' OR LOWER(a.email) LIKE LOWER(CONCAT('%', :email, '%'))) AND " +
	       "(:status IS NULL OR :status = '' OR LOWER(a.status) = LOWER(:status))")
	List<AdopterEntity> searchAdopters(@Param("name") String name, @Param("email") String email, @Param("status") String status);
}
