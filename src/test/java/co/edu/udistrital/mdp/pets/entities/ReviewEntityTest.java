package co.edu.udistrital.mdp.pets.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

@DataJpaTest
class ReviewEntityTest {

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private ReviewEntity reviewEntity;

    @BeforeEach
    void setUp() {
        reviewEntity = factory.manufacturePojo(ReviewEntity.class);
    }

    @Test
    void testCreateReview() {
        ReviewEntity saved = entityManager.persistAndFlush(reviewEntity);
        ReviewEntity found = entityManager.find(ReviewEntity.class, saved.getId());

        assertNotNull(found);
        assertEquals(reviewEntity.getRating(), found.getRating());
        assertEquals(reviewEntity.getComment(), found.getComment());
        assertEquals(reviewEntity.getIsSuccessStory(), found.getIsSuccessStory());
    }

    @Test
    void testReviewWithAdopterAndShelter() {
        AdopterEntity adopter = factory.manufacturePojo(AdopterEntity.class);
        entityManager.persistAndFlush(adopter);

        ShelterEntity shelter = factory.manufacturePojo(ShelterEntity.class);
        entityManager.persistAndFlush(shelter);

        reviewEntity.setAdopter(adopter);
        reviewEntity.setShelter(shelter);
        ReviewEntity saved = entityManager.persistAndFlush(reviewEntity);

        ReviewEntity found = entityManager.find(ReviewEntity.class, saved.getId());
        assertNotNull(found);
        assertNotNull(found.getAdopter());
        assertNotNull(found.getShelter());
        assertEquals(adopter.getId(), found.getAdopter().getId());
        assertEquals(shelter.getId(), found.getShelter().getId());
    }
}
