package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@Import(ReviewService.class)
class ReviewServiceTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private TestEntityManager entityManager;

    private PodamFactory factory = new PodamFactoryImpl();

    private List<ReviewEntity> reviewList = new ArrayList<>();
    private List<AdopterEntity> adopterList = new ArrayList<>();
    private List<ShelterEntity> shelterList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ReviewEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdopterEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from ShelterEntity").executeUpdate();
    }

    private void insertData() {
        for (int i = 0; i < 3; i++) {
            AdopterEntity adopterEntity = factory.manufacturePojo(AdopterEntity.class);
            entityManager.persist(adopterEntity);
            adopterList.add(adopterEntity);
        }

        for (int i = 0; i < 3; i++) {
            ShelterEntity shelterEntity = factory.manufacturePojo(ShelterEntity.class);
            entityManager.persist(shelterEntity);
            shelterList.add(shelterEntity);
        }

        for (int i = 0; i < 3; i++) {
            ReviewEntity reviewEntity = factory.manufacturePojo(ReviewEntity.class);
            entityManager.persist(reviewEntity);
            reviewList.add(reviewEntity);
        }
    }

    @Test
    void testCreateReview() throws IllegalOperationException {
        ReviewEntity newEntity = factory.manufacturePojo(ReviewEntity.class);

        ReviewEntity result = reviewService.createReview(newEntity);
        assertNotNull(result);

        ReviewEntity entity = entityManager.find(ReviewEntity.class, result.getId());
        assertEquals(newEntity.getId(), entity.getId());
        assertEquals(newEntity.getRating(), entity.getRating());
        assertEquals(newEntity.getComment(), entity.getComment());
        assertEquals(newEntity.getIsSuccessStory(), entity.getIsSuccessStory());
    }

    @Test
    void testCreateReviewNull() {
        assertThrows(IllegalOperationException.class, () -> reviewService.createReview(null));
    }

    @Test
    void testSearchReview() throws EntityNotFoundException {
        ReviewEntity entity = reviewList.get(0);
        ReviewEntity resultEntity = reviewService.searchReview(entity.getId());
        assertNotNull(resultEntity);
        assertEquals(entity.getId(), resultEntity.getId());
        assertEquals(entity.getRating(), resultEntity.getRating());
        assertEquals(entity.getComment(), resultEntity.getComment());
        assertEquals(entity.getIsSuccessStory(), resultEntity.getIsSuccessStory());
    }

    @Test
    void testSearchReviewNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            reviewService.searchReview(0L);
        });
    }

    @Test
    void testSearchReviews() {
        List<ReviewEntity> list = reviewService.searchReviews();
        assertEquals(reviewList.size(), list.size());
        for (ReviewEntity entity : list) {
            boolean found = false;
            for (ReviewEntity storedEntity : reviewList) {
                if (entity.getId().equals(storedEntity.getId())) {
                    found = true;
                }
            }
            assertTrue(found);
        }
    }

    @Test
    void testUpdateReview() throws EntityNotFoundException, IllegalOperationException {
        ReviewEntity entity = reviewList.get(0);
        ReviewEntity pojoEntity = factory.manufacturePojo(ReviewEntity.class);
        pojoEntity.setId(entity.getId());

        reviewService.updateReview(entity.getId(), pojoEntity);

        ReviewEntity resp = entityManager.find(ReviewEntity.class, entity.getId());
        assertEquals(pojoEntity.getRating(), resp.getRating());
        assertEquals(pojoEntity.getComment(), resp.getComment());
        assertEquals(pojoEntity.getIsSuccessStory(), resp.getIsSuccessStory());
    }

    @Test
    void testUpdateReviewNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            ReviewEntity pojoEntity = factory.manufacturePojo(ReviewEntity.class);
            pojoEntity.setId(0L);
            reviewService.updateReview(0L, pojoEntity);
        });
    }

    @Test
    void testDeleteReview() throws EntityNotFoundException, IllegalOperationException {
        ReviewEntity entity = reviewList.get(1);
        reviewService.deleteReview(entity.getId());
        ReviewEntity deleted = entityManager.find(ReviewEntity.class, entity.getId());
        assertNull(deleted);
    }

    @Test
    void testDeleteReviewNotFound() {
        assertThrows(EntityNotFoundException.class, () -> {
            reviewService.deleteReview(0L);
        });
    }

    @Test
    void testDeleteReviewWithAdopter() {
        assertThrows(IllegalOperationException.class, () -> {
            ReviewEntity entity = reviewList.get(0);
            entity.setAdopter(adopterList.get(0));
            entityManager.merge(entity);
            reviewService.deleteReview(entity.getId());
        });
    }

    @Test
    void testDeleteReviewWithShelter() {
        assertThrows(IllegalOperationException.class, () -> {
            ReviewEntity entity = reviewList.get(0);
            entity.setShelter(shelterList.get(0));
            entityManager.merge(entity);
            reviewService.deleteReview(entity.getId());
        });
    }
}
