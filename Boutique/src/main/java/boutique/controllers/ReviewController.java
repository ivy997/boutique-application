package boutique.controllers;

import boutique.config.UserDetailsImpl;
import boutique.entities.Product;
import boutique.entities.Review;
import boutique.entities.User;
import boutique.models.*;
import boutique.repositories.ProductRepository;
import boutique.repositories.ReviewRepository;
import boutique.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/review")
@PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
public class ReviewController {
    private ReviewRepository reviewRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;

    public ReviewController(ReviewRepository reviewRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProcess(@RequestBody ReviewRequest reviewRequest,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            if (!this.productRepository.existsById(reviewRequest.getProductId())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Could not find product with id ${productId}."));
            }

            User user = userDetails.getUser();

            Product product = this.productRepository.findById(reviewRequest.getProductId()).get();

            Review review = new Review(
                    reviewRequest.getScore(),
                    reviewRequest.getComment(),
                    user, product);

            this.reviewRepository.saveAndFlush(review);

            return ResponseEntity.ok(new MessageResponse("Review created successfully!"));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not create review.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    /*@GetMapping("/{id}")
    public ResponseEntity<?> details(@PathVariable Integer id) {
        try {
            if (!this.reviewRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find review with id ${id}."));
            }

            Review review = this.reviewRepository.findById(id).get();
            ReviewResponse reviewResponse = this.parseReview(review);

            return ResponseEntity.ok(reviewResponse);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not get review details.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }
*/
    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or #userDetails.user.id == #id")
    public ResponseEntity<?> edit(@PathVariable Integer id,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            if (!this.reviewRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find review with id ${id}."));
            }

            Review review = this.reviewRepository.findById(id).get();
            ReviewResponse reviewResponse = this.parseReview(review);

            return ResponseEntity.ok(reviewResponse);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not get review details.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ADMIN') or #userDetails.user.id == #id")
    public ResponseEntity<?> editProcess(@PathVariable Integer id,
                                         @RequestBody ReviewRequest reviewRequest,
                                         @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            if (!this.reviewRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find review with id ${id}."));
            }

            Review review = this.reviewRepository.findById(id).get();

            if (reviewRequest.getScore() != null) {
                review.setScore(reviewRequest.getScore());
            }

            if (reviewRequest.getComment() != null) {
                review.setComment(reviewRequest.getComment());
            }

            this.reviewRepository.saveAndFlush(review);

            return ResponseEntity.ok(new MessageResponse("Review edited successfully!"));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not edit product.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }


    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or #userDetails.user.id == #id")
    public ResponseEntity<?> delete(@PathVariable Integer id,
                                    @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            if (!this.reviewRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find review with id ${id}."));
            }

            Review review = this.reviewRepository.findById(id).get();
            ReviewResponse reviewResponse = this.parseReview(review);

            return ResponseEntity.ok(reviewResponse);
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not get review details.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN') or #userDetails.user.id == #id")
    public ResponseEntity<?> deleteProcess(@PathVariable Integer id,
                                           @AuthenticationPrincipal UserDetailsImpl userDetails) {
        try {
            if (!this.reviewRepository.existsById(id)) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse(
                                "Error: Could not find review with id ${id}."));
            }

            Review review = this.reviewRepository.findById(id).get();

            this.reviewRepository.delete(review);

            return ResponseEntity.ok(new MessageResponse("Review deleted successfully!"));
        }
        catch (Exception ex) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Could not delete review.", Arrays.stream(ex
                            .getStackTrace()).map(x -> x.toString())
                            .collect(Collectors.joining(", "))));
        }
    }

    private ReviewResponse parseReview(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getScore(),
                review.getComment(),
                new UserResponse(
                        review.getUser().getId(),
                        review.getUser().getName(),
                        review.getUser().getSurname(),
                        review.getUser().getAddress(),
                        review.getUser().getEmail()),
                new ProductResponse(
                        review.getProduct().getId(),
                        review.getProduct().getName(),
                        review.getProduct().getDescription(),
                        review.getProduct().getPicture(),
                        review.getProduct().getDiscount(),
                        review.getProduct().getPrice()));
    }
}
