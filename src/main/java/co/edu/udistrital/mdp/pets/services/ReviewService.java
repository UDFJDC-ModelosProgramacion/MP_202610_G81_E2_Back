package co.edu.udistrital.mdp.pets.services;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.repositories.ReviewRepository;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReviewService {
    private static final String NOT_FOUND_MSG = "Review not found.";
    private static final String REVIEW_ID_NULL_MSG = "Review id cannot be null";

    private static final String REV_NOT_FOUND = NOT_FOUND_MSG;

    @Autowired
    ReviewRepository reviewRepository;

    @Transactional
    public ReviewEntity createReview(ReviewEntity review) throws IllegalOperationException {
        log.info("Inicia proceso de creación de la reseña");

        if (review == null) {
            throw new IllegalOperationException("Review cannot be null.");
        }

        log.info("Termina proceso de creación de la reseña");
        return reviewRepository.save(review);
    }

    @Transactional
    public ReviewEntity searchReview(Long id) throws EntityNotFoundException {
        log.info("Inicia proceso de consultar la reseña con id = {}", id);
        Objects.requireNonNull(id, REVIEW_ID_NULL_MSG);

        Optional<ReviewEntity> reviewEntity = reviewRepository.findById(id);
        if (reviewEntity.isEmpty())
            throw new EntityNotFoundException(REV_NOT_FOUND);

        log.info("Termina proceso de consultar la reseña con id = {}", id);
        return reviewEntity.get();
    }

    @Transactional
    public List<ReviewEntity> searchReviews() {
        log.info("Inicia proceso de consultar todas las reseñas");
        return reviewRepository.findAll();
    }

    @Transactional
    public ReviewEntity updateReview(Long id, ReviewEntity review)
            throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de actualizar la reseña con id = {}", id);
        Objects.requireNonNull(id, REVIEW_ID_NULL_MSG);

        Optional<ReviewEntity> reviewEntity = reviewRepository.findById(id);
        if (reviewEntity.isEmpty())
            throw new EntityNotFoundException(REV_NOT_FOUND);

        ReviewEntity existing = reviewEntity.get();
        existing.setRating(review.getRating());
        existing.setComment(review.getComment());
        existing.setIsSuccessStory(review.getIsSuccessStory());

        log.info("Termina proceso de actualizar la reseña con id = {}", id);
        return reviewRepository.save(existing);
    }

    @Transactional
    public void deleteReview(Long id) throws EntityNotFoundException, IllegalOperationException {
        log.info("Inicia proceso de borrar la reseña con id = {}", id);
        Objects.requireNonNull(id, REVIEW_ID_NULL_MSG);

        Optional<ReviewEntity> reviewEntity = reviewRepository.findById(id);
        if (reviewEntity.isEmpty())
            throw new EntityNotFoundException(REV_NOT_FOUND);

        ReviewEntity review = reviewEntity.get();

        if (review.getAdopter() != null) {
            throw new IllegalOperationException("Cannot delete review with associated Adopter");
        }

        if (review.getShelter() != null) {
            throw new IllegalOperationException("Cannot delete review with associated Shelter");
        }

        reviewRepository.delete(review);
        log.info("Termina proceso de borrar la reseña con id = {}", id);
    }
}
