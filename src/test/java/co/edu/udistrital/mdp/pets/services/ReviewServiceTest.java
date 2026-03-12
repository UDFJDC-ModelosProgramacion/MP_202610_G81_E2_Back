package co.edu.udistrital.mdp.pets.services;

import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import jakarta.transaction.Transactional;

@DataJpaTest
@Transactional
@Import(ReviewService.class)
public class ReviewServiceTest {
    
}
