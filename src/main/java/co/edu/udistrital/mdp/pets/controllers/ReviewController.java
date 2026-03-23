package co.edu.udistrital.mdp.pets.controllers;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private List<Map<String, Object>> reviews = new ArrayList<>();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, Object> createReview(@RequestBody Map<String, Object> review){

        if (!review.containsKey("adopterId") ||
            !review.containsKey("shelterId") ||
            !review.containsKey("adoptionProcessId") ||
            !review.containsKey("rating")) {

            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing fields");
        }

        review.put("id", reviews.size() + 1);
        review.put("isSuccessStory", review.getOrDefault("isSuccessStory", false));

        reviews.add(review);
        return review;
    }

    @GetMapping
    public List<Map<String, Object>> getReviews(
            @RequestParam(required = false) Long adopterId,
            @RequestParam(required = false) Long shelterId,
            @RequestParam(required = false) Long adoptionProcessId){

        List<Map<String, Object>> result = new ArrayList<>();

        for (Map<String, Object> r : reviews) {

            boolean match = true;

            if (adopterId != null) {
                Long aid = Long.valueOf(r.get("adopterId").toString());
                match = match && aid.equals(adopterId);
            }

            if (shelterId != null) {
                Long sid = Long.valueOf(r.get("shelterId").toString());
                match = match && sid.equals(shelterId);
            }

            if (adoptionProcessId != null) {
                Long apid = Long.valueOf(r.get("adoptionProcessId").toString());
                match = match && apid.equals(adoptionProcessId);
            }

            if (match) result.add(r);
        }

        return result;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getReviewById(@PathVariable Long id){

        return reviews.stream()
            .filter(r -> Long.valueOf(r.get("id").toString()).equals(id))
            .findFirst()
            .orElseThrow(() -> new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Review not found"
            ));
    }

    @PutMapping("/{id}")
    public Map<String, Object> updateReview(@PathVariable Long id,
                                            @RequestBody Map<String, Object> updatedReview){

        for (Map<String, Object> r : reviews) {
            Long rid = Long.valueOf(r.get("id").toString());

            if (rid.equals(id)) {
                r.putAll(updatedReview);
                r.put("id", id);
                return r;
            }
        }

        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteReview(@PathVariable Long id){

        boolean removed = reviews.removeIf(r ->
            Long.valueOf(r.get("id").toString()).equals(id)
        );

        if (!removed) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found");
        }
    }
}