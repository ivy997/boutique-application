package boutique.services;

import boutique.entities.Review;
import boutique.models.ProductResponse;
import boutique.models.ReviewResponse;
import boutique.models.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    public List<ReviewResponse> parseReviews(Page<Review> pagedResult) {
        List<ReviewResponse> reviews = pagedResult
                .stream().map(x -> new ReviewResponse(
                        x.getId(),
                        x.getScore(),
                        x.getComment(),
                        new UserResponse(
                                x.getUser().getId(),
                                x.getUser().getName(),
                                x.getUser().getSurname(),
                                x.getUser().getAddress(),
                                x.getUser().getEmail()),
                        new ProductResponse(
                                x.getProduct().getId(),
                                x.getProduct().getName(),
                                x.getProduct().getDescription(),
                                x.getProduct().getPicture(),
                                x.getProduct().getDiscount(),
                                x.getProduct().getPrice()))
                ).collect(Collectors.toList());

        return reviews;
    }
}
