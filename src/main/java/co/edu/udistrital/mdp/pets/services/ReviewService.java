package co.edu.udistrital.mdp.pets.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.udistrital.mdp.pets.entities.ReviewEntity;
import co.edu.udistrital.mdp.pets.repositories.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class ReviewService {
    @Autowired
    ReviewRepository reviewRepository;

    public ReviewEntity createReview(ReviewEntity review){
        log.info("Creating review");

        if(review == null){
            throw new IllegalArgumentException("Review cannot be null.");
        }

        return reviewRepository.save(review);
    }

    @SuppressWarnings("null")
    public ReviewEntity searchReview(Long id){
        log.info("Search a review with id ={}", id);

        return reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review not found."));
    }

    public List<ReviewEntity> searchReviews(){
        log.info("Search all reviews.");

        return reviewRepository.findAll();
    }

    public ReviewEntity updateReview(Long id, ReviewEntity review){
        log.info("Updatig review with id = {}", id);

        @SuppressWarnings("null")
        ReviewEntity existing = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review cannot be found."));

        existing.setRating(review.getRating());
        existing.setComment(review.getComment());
        existing.setIsSuccessStory(review.getIsSuccessStory());

        return reviewRepository.save(existing);
    }

    public void deleteReview(Long id){
        log.info("Delete review with id = {}", id);

        @SuppressWarnings("null")
        ReviewEntity review = reviewRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Review cannot be found."));

        if(review.getAdopter() != null){
            throw new IllegalArgumentException("Cannot delete review with associated Adopter");
        }

        if(review.getShelter() != null){
            throw new IllegalArgumentException("Cannot delete review with associated Sheleter");
        }

        reviewRepository.delete(review);
    }
}
