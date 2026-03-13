package co.edu.udistrital.mdp.pets.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

import co.edu.udistrital.mdp.pets.entities.AdoptionProcessEntity;
import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import jakarta.transaction.Transactional;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

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
    private List<AdoptionProcessEntity> adoptionProcessList = new ArrayList<>();

    @BeforeEach
    void setUp() {
        clearData();
        insertData();
    }

    private void clearData() {
        entityManager.getEntityManager().createQuery("delete from ReviewEntity").executeUpdate();
        entityManager.getEntityManager().createQuery("delete from AdoptionEntity").executeUpdate();
    }

    private void insertData() {

        for (int i = 0; i < 3; i++) {
            AdoptionProcessEntity adoption = factory.manufacturePojo(AdoptionProcessEntity.class);
            entityManager.persist(adoption);
            adoptionProcessList.add(adoption);
        }

        for (int i = 0; i < 3; i++) {
            ReviewEntity review = factory.manufacturePojo(ReviewEntity.class);
            review.setAdoptionProcess(adoptionProcessList.get(0));
            entityManager.persist(review);
            reviewList.add(review);
        }
    }

    @Test
    void createReviewTest() {

        ReviewEntity newEntity = factory.manufacturePojo(ReviewEntity.class);
        newEntity.setAdoptionProcess(adoptionProcessList.get(0));

        ReviewEntity result = reviewService.createReview(newEntity);

        assertNotNull(result);

        ReviewEntity entity = entityManager.find(ReviewEntity.class, result.getId());

        assertEquals(newEntity.getRating(), entity.getRating());
        assertEquals(newEntity.getComment(), entity.getComment());
    }

    @Test
    void searchReviewTest(){

        ReviewEntity entity = reviewList.get(0);

        ReviewEntity result = reviewService.searchReview(entity.getId());

        assertNotNull(result);
        assertEquals(entity.getId(), result.getId());
    }

    @Test
    void searchReviewsTest(){

        List<ReviewEntity> list = reviewService.searchReviews();

        assertEquals(reviewList.size(), list.size());
    }

    @Test
    void updateReviewTest(){

        ReviewEntity entity = reviewList.get(0);

        ReviewEntity newEntity = factory.manufacturePojo(ReviewEntity.class);
        newEntity.setId(entity.getId());
        newEntity.setAdoptionProcess(entity.getAdoptionProcess());

        reviewService.updateReview(entity.getId(), newEntity);

        ReviewEntity resp = entityManager.find(ReviewEntity.class, entity.getId());

        assertEquals(newEntity.getRating(), resp.getRating());
        assertEquals(newEntity.getComment(), resp.getComment());
    }

    @Test
    void deleteReviewTest(){

        ReviewEntity entity = reviewList.get(0);

        reviewService.deleteReview(entity.getId());

        ReviewEntity deleted = entityManager.find(ReviewEntity.class, entity.getId());

        assertNull(deleted);
    }
}