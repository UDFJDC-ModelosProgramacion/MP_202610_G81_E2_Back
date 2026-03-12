package co.edu.udistrital.mdp.pets.services;

import co.edu.udistrital.mdp.pets.entities.AdopterEntity;
import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.entities.ShelterEntity;
import co.edu.udistrital.mdp.pets.repositories.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    private PodamFactory factory = new PodamFactoryImpl();
    private ReviewEntity review;

    @BeforeEach
    void setUp() {
        review = factory.manufacturePojo(ReviewEntity.class);
        review.setAdopter(null);
        review.setShelter(null);
    }

    @Test
    void testCreateReviewSuccess() {
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(review);

        ReviewEntity result = reviewService.createReview(review);
        assertNotNull(result);
        assertEquals(review.getId(), result.getId());
    }

    @Test
    void testCreateReviewNull() {
        assertThrows(IllegalArgumentException.class, () -> reviewService.createReview(null));
    }

    @Test
    void testSearchReviewSuccess() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        ReviewEntity result = reviewService.searchReview(review.getId());
        assertNotNull(result);
        assertEquals(review.getId(), result.getId());
    }

    @Test
    void testSearchReviewNotFound() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> reviewService.searchReview(review.getId()));
    }

    @Test
    void testSearchReviews() {
        List<ReviewEntity> list = Arrays.asList(review);
        when(reviewRepository.findAll()).thenReturn(list);
        List<ReviewEntity> result = reviewService.searchReviews();
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    void testUpdateReviewSuccess() {
        ReviewEntity updatedReview = new ReviewEntity();
        updatedReview.setRating(5);
        updatedReview.setComment("Updated Comment");
        updatedReview.setIsSuccessStory(true);

        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));
        when(reviewRepository.save(any(ReviewEntity.class))).thenReturn(updatedReview);

        ReviewEntity result = reviewService.updateReview(review.getId(), updatedReview);
        assertNotNull(result);
        assertEquals("Updated Comment", result.getComment());
        assertEquals(5, result.getRating());
        assertEquals(true, result.getIsSuccessStory());
    }

    @Test
    void testUpdateReviewNotFound() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> reviewService.updateReview(review.getId(), review));
    }

    @Test
    void testDeleteReviewSuccess() {
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        assertDoesNotThrow(() -> reviewService.deleteReview(review.getId()));
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    void testDeleteReviewWithAdopter() {
        AdopterEntity adopter = new AdopterEntity();
        review.setAdopter(adopter);
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        assertThrows(IllegalArgumentException.class, () -> reviewService.deleteReview(review.getId()));
        verify(reviewRepository, never()).delete(any());
    }

    @Test
    void testDeleteReviewWithShelter() {
        ShelterEntity shelter = new ShelterEntity();
        review.setShelter(shelter);
        when(reviewRepository.findById(review.getId())).thenReturn(Optional.of(review));

        assertThrows(IllegalArgumentException.class, () -> reviewService.deleteReview(review.getId()));
        verify(reviewRepository, never()).delete(any());
    }
}
