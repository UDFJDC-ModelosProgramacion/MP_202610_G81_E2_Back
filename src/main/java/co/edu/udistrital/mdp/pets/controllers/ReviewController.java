package co.edu.udistrital.mdp.pets.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import co.edu.udistrital.mdp.pets.dto.ReviewDTO;
import co.edu.udistrital.mdp.pets.dto.ReviewDetailDTO;
import co.edu.udistrital.mdp.pets.exceptions.EntityNotFoundException;
import co.edu.udistrital.mdp.pets.exceptions.IllegalOperationException;
import co.edu.udistrital.mdp.pets.services.ReviewService;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    @ResponseStatus(code = HttpStatus.OK)
    public List<ReviewDTO> findAll() {
        return reviewService.searchReviews().stream().map(ReviewDTO::new).toList();
    }

    @GetMapping(value = "/{reviewId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ReviewDetailDTO findOne(@PathVariable Long reviewId) throws EntityNotFoundException {
        return new ReviewDetailDTO(reviewService.searchReview(reviewId));
    }

    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public ReviewDetailDTO create(@RequestBody ReviewDTO reviewDTO)
            throws IllegalOperationException {
        return new ReviewDetailDTO(reviewService.createReview(reviewDTO.toEntity()));
    }

    @PutMapping(value = "/{reviewId}")
    @ResponseStatus(code = HttpStatus.OK)
    public ReviewDetailDTO update(@PathVariable Long reviewId,
            @RequestBody ReviewDTO reviewDTO)
            throws EntityNotFoundException, IllegalOperationException {
        return new ReviewDetailDTO(reviewService.updateReview(reviewId, reviewDTO.toEntity()));
    }

    @DeleteMapping(value = "/{reviewId}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long reviewId)
            throws EntityNotFoundException, IllegalOperationException {
        reviewService.deleteReview(reviewId);
    }
}